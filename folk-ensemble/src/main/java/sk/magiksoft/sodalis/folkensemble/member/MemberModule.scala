/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.folkensemble.member

import action.{RemoveMemberAction, AddMemberAction}
import data.EnsembleGroupDynamicCategory
import entity.property.MemberPropertyTranslator
import entity.{UniversityData, EnsembleData, MemberData}
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.person.entity.{PersonWrapper, PersonHistoryData, PrivatePersonData}
import sk.magiksoft.sodalis.person.imex.PersonImportResolver
import collection.JavaConversions
import collection.mutable.ArrayBuffer
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.person.data.SexDynamicCategory
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.category.entity.{Category, Categorized, EntityDynamicCategory, DynamicCategory}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/22/11
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

class MemberModule extends AbstractModule with PersonModule {
  private lazy val dynamicCategories = createDynamicCategories

  private def createDynamicCategories = {
    val rootCategory = CategoryManager.getInstance.getRootCategory(classOf[MemberModule], false)
    val categories = new ArrayBuffer[DynamicCategory]

    categories += new SexDynamicCategory(rootCategory)
    categories += new EnsembleGroupDynamicCategory(rootCategory)
    if (SodalisApplication.get.getModuleManager.isModulePresent(classOf[RepertoryModule])) {
      categories += new EntityDynamicCategory[Song, Person](LocaleManager.getString("songInterpretation"), "select s from Song s where size(s.interpreters)>0") {
        id = -1
        parentCategory = rootCategory

        def acceptCategorized(entity: Song, categorized: Person) =
          entity.getInterpreters.exists {
            pw => (pw.getPerson ne null) && (pw.getPerson.id == categorized.getId)
          }
      }
    }
    if (SodalisApplication.get.getModuleManager.isModulePresent(classOf[ProgrammeModule])) {
      categories += new EntityDynamicCategory[Programme, Person](LocaleManager.getString("programme"), "select p from Programme p left join p.programmeSongs as ps where size(p.programmeSongs)>0 and size(ps.interpreters)>0") {
        id = -2
        parentCategory = rootCategory

        def acceptCategorized(entity: Programme, categorized: Person) =
          entity.getInterpreters.exists {
            pw => (pw.getPerson ne null) && (pw.getPerson.id == categorized.getId)
          }
      }
    }

    categories
  }

  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.member.locale.member")
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.event.locale.event")

  EntityFactory.getInstance.registerEntityProperties(classOf[Person], classOf[PrivatePersonData], classOf[PersonHistoryData], classOf[MemberData], classOf[UniversityData], classOf[EnsembleData])
  EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EnsembleEventData])
  EntityPropertyTranslatorManager.registerTranslator(classOf[Person], new MemberPropertyTranslator)
  ImExManager.registerImportProcessor(classOf[Person], new PersonImportResolver)
  ImExManager.registerImportProcessor(classOf[PersonWrapper], new PersonWrapperImportResolver)

  def getDataListener = MemberContextManager

  def getContextManager = MemberContextManager

  def getModuleDescriptor = new ModuleDescriptor(IconFactory.getInstance().getIcon("folkEnsembleMemberModule").asInstanceOf[ImageIcon], LocaleManager.getString("members"))

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }
}