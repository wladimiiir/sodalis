/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.imex

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import tools.nsc.io.File
import scala.collection.JavaConversions._
import java.io.{File => jFile}
import io.Codec
import com.thoughtworks.xstream.mapper.Mapper
import com.thoughtworks.xstream.converters.collections.{MapConverter, CollectionConverter}
import java.util.{ArrayList => jArrayList, Map => jMap, HashMap => jHashMap, Collection => jCollection, List => jList}
import sk.magiksoft.sodalis.core.imex.ScalaXStream._
import collection.mutable.{ListBuffer, HashMap}
import sk.magiksoft.sodalis.core.logger.LoggerManager
import com.thoughtworks.xstream.io.{HierarchicalStreamReader, HierarchicalStreamWriter}
import com.thoughtworks.xstream.converters.{UnmarshallingContext, Converter, MarshallingContext}
import java.math.{RoundingMode, MathContext}
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import org.hibernate.Hibernate
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter
import org.hibernate.proxy.HibernateProxy
import org.hibernate.collection.internal._
import org.hibernate.collection.spi.PersistentCollection
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/4/11
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */

object ImExManager extends App {
  private lazy val xStream = new XStream(new DomDriver)
  private lazy val importProcessorMap = new HashMap[Class[_], ImportProcessor[_ <: DatabaseEntity]]

  initXStream()

  private def initXStream() {
    implicit val mapper = xStream.getMapper

    xStream.setMode(XStream.ID_REFERENCES)

    xStream.alias("sodalis", classOf[SodalisTag])
    xStream.addImplicitCollection(classOf[SodalisTag], "collection")
    xStream.registerConverter(new PersistentCollectionConverter(xStream.getMapper))
    xStream.registerConverter(new PersistentMapConverter(xStream.getMapper))
    xStream.registerConverter(new HibernateProxyConverter)
    xStream.registerConverter(new ScalaEnumerationConverter)
    xStream.registerConverter(new ScalaListConverter)
    xStream.registerConverter(new ScalaSeqConverter[ListBuffer[Any]](seq => new ListBuffer[Any] ++= seq))
    xStream.registerConverter(new ScalaSymbolConverter)
    xStream.registerConverter(new ScalaTupleConverter)
    xStream.registerConverter(new MathContextConverter)
    xStream.alias("java.util.ArrayList", classOf[PersistentBag])
    xStream.alias("java.util.HashMap", classOf[PersistentMap])
    xStream.omitField(classOf[AbstractDatabaseEntity], "id")
    xStream.omitField(classOf[AbstractDatabaseEntity], "updater")
    xStream.omitField(classOf[AbstractDatabaseEntity], "readableUpdater")
    xStream.addDefaultImplementation(classOf[jArrayList[_]], classOf[PersistentBag])
    xStream.addDefaultImplementation(classOf[jHashMap[_, _]], classOf[PersistentMap])
    xStream.addDefaultImplementation(classOf[jArrayList[_]], classOf[jCollection[_]])
  }

  def registerImportProcessor[T <: DatabaseEntity](clazz: Class[T], processor: ImportProcessor[T]) {
    importProcessorMap += clazz -> processor
  }

  def getImportProcessor[T <: DatabaseEntity](clazz: Class[T]) = {
    importProcessorMap(clazz).asInstanceOf[ImportProcessor[T]]
  }

  def processEntity[T <: DatabaseEntity](entity: T): T = {
    def processClass(clazz: Class[_]): T = clazz match {
      case clazz: Class[_] if importProcessorMap.contains(clazz) =>
        importProcessorMap(clazz).asInstanceOf[ImportProcessor[T]].processImport(entity)
      case clazz: Class[_] => processClass(clazz.getSuperclass)
      case _ => entity
    }

    processClass(entity.getClass)
  }

  def importFile(file: jFile): jList[_] = importScalaFile(File(file))

  def importScalaFile(importFile: File) = {
    val reader = importFile.reader(Codec.UTF8)

    try {
      val sodalisTag = xStream.fromXML(reader).asInstanceOf[SodalisTag]
      sodalisTag.getCollection.map(processEntity(_)).toList
    } catch {
      case e: Exception => {
        LoggerManager.getInstance().error(getClass, e)
        throw e
      }
    } finally {
      reader.close()
    }
  }

  def exportDataScala(file: File, entities: List[_]) {
    val writer = file.writer(false, Codec.UTF8)
    try {
      //      writer.write(DefaultDataManager.getInstance().exportEntitiesToXML(new SodalisTag(entities), xStream))
      writer.write(xStream.toXML(new SodalisTag(entities)));
    } finally {
      writer.close()
    }
  }

  def exportData(file: jFile, entities: jList[_]) {
    exportDataScala(new File(file), entities.toList)
  }

  private class MathContextConverter extends Converter {
    def canConvert(clazz: Class[_]) = clazz == classOf[MathContext]

    def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext) = {
      reader.moveDown()
      val precision = reader.getValue.toInt
      reader.moveUp()
      reader.moveDown()
      val roundingMode = RoundingMode.values()(reader.getValue.toInt)
      reader.moveUp()
      new MathContext(precision, roundingMode)
    }

    def marshal(source: AnyRef, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val mc = source.asInstanceOf[MathContext]
      writer.startNode("precision")
      writer.setValue(mc.getPrecision.toString)
      writer.endNode()
      writer.startNode("roundingMode")
      writer.setValue(mc.getRoundingMode.ordinal().toString)
      writer.endNode()
    }
  }

  private class PersistentCollectionConverter(mapper: Mapper) extends CollectionConverter(mapper) {
    override def marshal(source: AnyRef, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val collection = source.asInstanceOf[PersistentCollection]
      if (!Hibernate.isInitialized(collection)) {
        super.marshal(new jArrayList(DefaultDataManager.getInstance().initialize(collection).asInstanceOf[jCollection[_]]), writer, context)
      } else {
        super.marshal(new jArrayList(collection.asInstanceOf[jCollection[_]]), writer, context)
      }
    }

    override def canConvert(clazz: Class[_]) = {
      clazz == classOf[PersistentBag] || clazz == classOf[PersistentList] ||
        clazz == classOf[PersistentSet] || clazz == classOf[PersistentSortedSet]
    }
  }

  private class HibernateProxyConverter(implicit mapper: Mapper) extends JavaBeanConverter(mapper) {
    override def marshal(source: AnyRef, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      super.marshal(DefaultDataManager.getInstance().initialize(source), writer, context)
    }

    override def canConvert(clazz: Class[_]) = {
      classOf[HibernateProxy].isAssignableFrom(clazz)
    }
  }

  private class PersistentMapConverter(mapper: Mapper) extends MapConverter(mapper) {
    override def marshal(source: AnyRef, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val map = source.asInstanceOf[jMap[Any, Any]]
      val hashMap = new jHashMap[Any, Any]

      for (entry <- map.entrySet()) {
        hashMap.put(entry.getKey, entry.getValue)
      }
      super.marshal(hashMap, writer, context)
    }

    override def canConvert(clazz: Class[_]) = {
      clazz == classOf[PersistentMap] || clazz == classOf[PersistentSortedMap]
    }
  }


}