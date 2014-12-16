package sk.magiksoft.sodalis.folkensemble.repertory

import java.util.ResourceBundle

import entity.property.SongPropertyTranslator
import entity.{SongHistoryData, Song}
import imex.SongImportResolver
import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.person.data.PersonWrapperDynamicCategory
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.member.data.EnsembleGroupDynamicCategory
import sk.magiksoft.sodalis.category.entity.{EntityDynamicCategory, Categorized}
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.core.enumeration.{EnumerationDynamicCategory, Enumerations}
import java.lang.String

/**
 * @author wladimiiir
 * @since 2011/4/22
 */
@DynamicModule
class RepertoryModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.folkensemble.locale.repertory"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("repertoryModule").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("repertory.moduleName"))

  private lazy val dynamicCategories = createDynamicCategories

  private def createDynamicCategories = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[RepertoryModule], false)
    List(
      new PersonWrapperDynamicCategory[Song](LocaleManager.getString("musicComposing"), "select s.composers from Song s") {
        setParentCategory(moduleCategory)
        setId(-10l)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getComposers.exists {
          p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
            (p.getPersonName == entity.getPersonName)
        }
      },
      new PersonWrapperDynamicCategory[Song](LocaleManager.getString("pedagogists"), "select s.pedagogists from Song s") {
        setParentCategory(moduleCategory)
        setId(-20l)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getPedagogists.exists {
          p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
            (p.getPersonName == entity.getPersonName)
        }
      },
      new PersonWrapperDynamicCategory[Song](LocaleManager.getString("choreography"), "select s.choreographers from Song s") {
        setParentCategory(moduleCategory)
        setId(-30l)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getChoreographers.exists {
          p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
            (p.getPersonName == entity.getPersonName)
        }
      },
      new PersonWrapperDynamicCategory[Song](LocaleManager.getString("interpretation"), "select s.interpreters from Song s") {
        setParentCategory(moduleCategory)
        setId(-40l)

        protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getInterpreters.exists {
          p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
            (p.getPersonName == entity.getPersonName)
        }
      },
      new EnsembleGroupDynamicCategory(moduleCategory) {
        setId(-50l)

        override def acceptEnsembleGroup(categorized: Categorized, groupType: Int) = categorized.asInstanceOf[Song].getInterpreters.exists {
          p => (p.getPerson ne null) && super.acceptEnsembleGroup(p.getPerson, groupType)
        }
      },
      new EntityDynamicCategory[Programme, Song](LocaleManager.getString("programme"),
        "select p from Programme p where size(p.programmeSongs)>0") {
        setParentCategory(moduleCategory)
        setId(-60l)

        protected def acceptCategorized(entity: Programme, categorized: Song) = entity.getProgrammeSongs.exists {
          ps => ps.getSong.getId == categorized.getId
        }
      },
      new EnumerationDynamicCategory(Enumerations.SONG_GENRE) {
        setParentCategory(moduleCategory)
        setId(-70l)

        def acceptEntryText(entryText: String, categorized: Categorized) =
          entryText.equalsIgnoreCase(categorized.asInstanceOf[Song].getGenre)
      },
      new EnumerationDynamicCategory(Enumerations.FOLK_REGION) {
        setParentCategory(moduleCategory)
        setId(-80l)

        def acceptEntryText(entryText: String, categorized: Categorized) =
          entryText.equalsIgnoreCase(categorized.asInstanceOf[Song].getRegion)
      }
    )
  }

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityFactory.getInstance.registerEntityProperties(classOf[Song], classOf[SongHistoryData])
    EntityPropertyTranslatorManager.registerTranslator(classOf[Song], new SongPropertyTranslator)
    ImExManager.registerImportProcessor(classOf[Song], new SongImportResolver)
  }

  def getDataListener = RepertoryContextManager.getInstance()

  def getContextManager = RepertoryContextManager.getInstance()

  def getModuleDescriptor = moduleDescriptor

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }
}
