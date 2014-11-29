package sk.magiksoft.sodalis.core.imex

import com.thoughtworks.xstream.mapper.Mapper
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter
import com.thoughtworks.xstream.io.{HierarchicalStreamReader, HierarchicalStreamWriter}
import com.thoughtworks.xstream.converters.{Converter, SingleValueConverter, UnmarshallingContext, MarshallingContext}
import java.lang.reflect.Field

/**
 * @author wladimiiir
 * @since 2011/6/4
 */

object ScalaXStream {

  class ScalaListConverter(implicit _mapper: Mapper) extends AbstractCollectionConverter(_mapper) {
    def canConvert(clazz: Class[_]) = {
      // "::" is the name of the list class, also handle nil
      classOf[::[_]] == clazz || Nil.getClass == clazz
    }

    def marshal(value: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val list = value.asInstanceOf[List[_]]
      for (item <- list) {
        writeItem(item, context, writer)
      }
    }

    def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext) = {
      val list = new scala.collection.mutable.ListBuffer[Any]()
      while (reader.hasMoreChildren) {
        reader.moveDown()
        val item = readItem(reader, context, list)
        list += item
        reader.moveUp()
      }
      list.toList
    }
  }

  import scala.collection.mutable.ListBuffer

  class ScalaSeqConverter[T <: Seq[Any]](fromSeq: Seq[Any] => T)(implicit manifest: ClassManifest[T], _mapper: Mapper) extends AbstractCollectionConverter(_mapper) {
    val seqClass = manifest.erasure

    def canConvert(clazz: Class[_]) = {
      seqClass == clazz
    }

    def marshal(value: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val list = value.asInstanceOf[Seq[_]]
      for (item <- list) {
        writeItem(item, context, writer)
      }
    }

    def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext) = {
      val list = new ListBuffer[Any]()
      while (reader.hasMoreChildren) {
        reader.moveDown()
        val item = readItem(reader, context, list)
        list += item
        reader.moveUp()
      }
      fromSeq(list)
    }
  }

  class ScalaTupleConverter(implicit _mapper: Mapper) extends AbstractCollectionConverter(_mapper) {

    def canConvert(clazz: Class[_]) = {
      clazz.getName.startsWith("scala.Tuple")
    }

    def marshal(value: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      val product = value.asInstanceOf[Product]
      for (item <- product.productIterator) {
        writeItem(item, context, writer)
      }
    }

    def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext) = {
      val list = new scala.collection.mutable.ListBuffer[AnyRef]()
      while (reader.hasMoreChildren) {
        reader.moveDown()
        val item = readItem(reader, context, list)
        list += item
        reader.moveUp()
      }
      constructors(list.size).newInstance(list: _*)
    }

    val constructors = 0 to 22 map {
      case 0 => null
      case i => Class.forName("scala.Tuple" + i).getConstructors.head.asInstanceOf[java.lang.reflect.Constructor[AnyRef]]
    }
  }


  class ScalaSymbolConverter extends SingleValueConverter {
    def toString(value: Any) =
      value.asInstanceOf[Symbol].name

    def fromString(name: String) =
      Symbol(name)

    def canConvert(clazz: Class[_]) =
      classOf[Symbol] == clazz
  }

  class ScalaEnumerationConverter extends Converter {
    def marshal(source: AnyRef, writer: HierarchicalStreamWriter, context: MarshallingContext) {
      try {
        val field: Field = source.getClass.getField("$outer")
        writer.startNode("enumClass")
        writer.setValue(field.get(source).getClass.getName)
        writer.endNode()
        writer.startNode("id")
        writer.setValue(String.valueOf((source.asInstanceOf[Enumeration#Value]).id))
        writer.endNode()
      }
      catch {
        case e: Exception => {
        }
      }
    }

    def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): AnyRef = {
      reader.moveDown()
      try {
        val enumeration = Class.forName(reader.getValue).getField("MODULE$").get(null).asInstanceOf[Enumeration]
        reader.moveUp()
        reader.moveDown()
        for (value <- enumeration.values) {
          if (String.valueOf((value.asInstanceOf[Enumeration#Value]).id) == reader.getValue) {
            return value
          }
        }
      }
      catch {
        case e: Exception => {
        }
      }
      finally {
        reader.moveUp()
      }
      null
    }

    def canConvert(clazz: Class[_]): Boolean = classOf[Enumeration#Value].isAssignableFrom(clazz)
  }

}
