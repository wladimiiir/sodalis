package sk.magiksoft.sodalis.core

import java.io.File
import java.net.URL

/**
 * @author wladimiiir 
 * @since 2014/12/29
 */
object ClassPathLoaderInjectors {
  def injectLibraryDir(directory: File): Unit = {
    directory.listFiles().filter(_.getName.endsWith(".jar")).foreach(injectLibraryFile)
  }

  def injectLibraryFile(file: File): Unit = {
    val classLoader = ClassLoader.getSystemClassLoader
    val addUrlMethod = classLoader.getClass.getDeclaredMethod("addUrl", classOf[URL])
    val accessible = addUrlMethod.isAccessible

    try {
      addUrlMethod.setAccessible(true)
      addUrlMethod.invoke(classLoader, file.toURI.toURL)
    } finally {

      addUrlMethod.setAccessible(accessible)
    }
  }
}
