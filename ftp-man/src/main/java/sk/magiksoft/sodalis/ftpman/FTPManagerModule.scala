/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman

import action.RetrieveFileAction
import entity.FTPEntry
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.core.enumeration.{EnumerationDynamicCategory, Enumerations}
import sk.magiksoft.sodalis.category.entity.{PropertyDynamicCategory, Categorized}
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sun.net.ftp.FtpProtocolException

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */

class FTPManagerModule extends AbstractModule {
  private lazy val descriptor = new ModuleDescriptor(IconFactory.getInstance().getIcon("").asInstanceOf[ImageIcon],
    LocaleManager.getString("ftpDirectory"))
  private lazy val dynamicCategories = createDynamicCategories

  RegistryManager.registerPopupAction(classOf[FTPEntry], new RetrieveFileAction)
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.ftpman.locale.ftpman")

  def getDataListener = FTPManager

  def getContextManager = FTPManager

  def getModuleDescriptor = descriptor

  private def createDynamicCategories = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[FTPManagerModule], false)
    List(
      new PropertyDynamicCategory[FTPEntry, String](classOf[FTPEntry], "host", LocaleManager.getString("host")) {
        setParentCategory(moduleCategory)
        setId(-10)

        protected def acceptProperty(entity: FTPEntry, value: String) = entity.host == value
      },
      new PropertyDynamicCategory[FTPEntry, String](classOf[FTPEntry], "path", LocaleManager.getString("path")) {
        setParentCategory(moduleCategory)
        setId(-20)

        protected def acceptProperty(entity: FTPEntry, value: String) = entity.path == value
      }
    )
  }

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }
}