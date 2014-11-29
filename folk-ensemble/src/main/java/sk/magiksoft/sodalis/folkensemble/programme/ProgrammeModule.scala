package sk.magiksoft.sodalis.folkensemble.programme

import entity.property.ProgrammePropertyTranslator
import entity.{ProgrammeSong, ProgrammeHistoryData, Programme}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.person.data.PersonWrapperDynamicCategory
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import collection.JavaConversions._
import sk.magiksoft.sodalis.category.entity.{EntityDynamicCategory, Category, Categorized}

/**
 * @author wladimiiir
 * @since 2011/4/22
 */

class ProgrammeModule extends AbstractModule {
  private lazy val dynamicCategories = createDynamicCategories
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("programmeModule").asInstanceOf[ImageIcon],
    LocaleManager.getString("programme.moduleName"))

  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.programme")
  EntityFactory.getInstance.registerEntityProperties(classOf[Programme], classOf[ProgrammeHistoryData])
  EntityPropertyTranslatorManager.registerTranslator(classOf[Programme], new ProgrammePropertyTranslator)

  private def createDynamicCategories = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[ProgrammeModule], false)
    List(
      new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("authors"), "select p.authors from Programme p") {
        setParentCategory(moduleCategory)
        setId(-10)

        protected def acceptCategorized(entity: PersonWrapper, programme: Programme) = {
          programme.getAuthors.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
            || (p.getPersonName == entity.getPersonName))
        }
      },
      new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("choreography"), "select p.choreographers from Programme p") {
        setParentCategory(moduleCategory)
        setId(-20)

        protected def acceptCategorized(entity: PersonWrapper, programme: Programme) = {
          programme.getChoreographers.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
            || (p.getPersonName == entity.getPersonName))
        }
      },
      new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("musicComposing"), "select p.composers from Programme p") {
        setParentCategory(moduleCategory)
        setId(-30)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Programme) = {
          categorized.getComposers.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
            || (p.getPersonName == entity.getPersonName))
        }
      },
      new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("interpretation"), "select ps.interpreters from Programme p left join p.programmeSongs as ps") {
        setParentCategory(moduleCategory)
        setId(-40)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Programme) = {
          categorized.getInterpreters.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
            || (p.getPersonName == entity.getPersonName))
        }
      },
      new EntityDynamicCategory[ProgrammeSong, Programme](LocaleManager.getString("song"), "select p.programmeSongs from Programme p") {
        setParentCategory(moduleCategory)
        setId(-50)

        override protected def getEntityString(entity: ProgrammeSong) = entity.getSong.getName

        override protected def getWrappedEntity(entity: ProgrammeSong) = entity.getSong

        protected def acceptCategorized(entity: ProgrammeSong, categorized: Programme) = categorized.getProgrammeSongs.exists {
          ps => ps.getSong.getId == entity.getSong.getId
        }
      }
    )
  }

  def getDataListener = ProgrammeContextManager.getInstance()

  def getContextManager = ProgrammeContextManager.getInstance()

  def getModuleDescriptor = moduleDescriptor

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }
}
