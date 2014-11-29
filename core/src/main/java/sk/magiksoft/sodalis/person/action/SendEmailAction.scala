package sk.magiksoft.sodalis.person.action

import sk.magiksoft.sodalis.core.action.EntityAction
import sk.magiksoft.sodalis.core.locale.LocaleManager
import collection.JavaConversions._
import sk.magiksoft.sodalis.person.entity.Contact.ContactType
import swing.Swing
import sk.magiksoft.sodalis.core.entity.EmailServer
import sk.magiksoft.sodalis.core.security.entity.User
import sk.magiksoft.sodalis.core.security.LoginManagerService
import sk.magiksoft.sodalis.core.security.util.SecurityUtils
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.person.entity.{InternetData, PrivatePersonData, Person}
import sk.magiksoft.sodalis.core.ui.{PasswordDialog, ISOptionPane, OkCancelDialog, EmailMessagePanel}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.Message.RecipientType
import javax.mail._
import sk.magiksoft.sodalis.core.logger.LoggerManager

/**
 * @author wladimiiir
 * @since 2011/3/28
 */

class SendEmailAction extends EntityAction[Person] {
  def getName(entities: List[Person]) = entities match {
    case entity :: Nil => LocaleManager.getString("sendEmail")
    case _ => LocaleManager.getString("sendEmails")
  }

  def isAllowed(entities: List[Person]) = entities.find(_.getPersonData(classOf[PrivatePersonData]).
    getContacts.find(_.getContactType == ContactType.EMAIL).isDefined).isDefined

  def apply(entities: List[Person]) {
    val user = SecurityUtils.getCredential(SodalisApplication.get.getService(classOf[LoginManagerService], LoginManagerService.SERVICE_NAME).getLoggedSubject, User.CREDENTIAL_PERSON).asInstanceOf[Person]
    val emailServer = user.getPersonData(classOf[InternetData]).getEmailServers.headOption

    emailServer match {
      case Some(emailServer) => {
        val emailPanel = new EmailMessagePanel
        val dialog = new OkCancelDialog(getName(entities)) {
          setSize(640, 480)
          setModal(true)
          setLocationRelativeTo(null)
          setMainPanel(emailPanel)
          getOkButton.addActionListener(Swing.ActionListener {
            _ => sendEmail(emailServer, emailPanel.getSubject, emailPanel.getMessage,
              entities.map {
                _.getPersonData(classOf[PrivatePersonData]).getContacts.find {
                  _.getContactType == ContactType.EMAIL
                }
              }
                .filter {
                _.isDefined
              }
                .map {
                _.get.getContact
              }
            )
          })
        }
        emailPanel.requestFocusInWindow
        dialog.setVisible(true)
      }
      case None => ISOptionPane.showMessageDialog(LocaleManager.getString("noEmailServer"))
    }
  }

  private def sendEmail(emailServer: EmailServer, subject: String, message: String, emailAdresses: List[String]) = {
    val passwordDialog = new PasswordDialog(LocaleManager.getString("typeEmailServerPassword")) {
      setSize(300, 100)
    }
    passwordDialog.showDialog match {
      case password: String => {
        try {
          val properties = new Properties {
            put("mail.smtp.starttls.enable", "true")
          }
          val session: Session = Session.getDefaultInstance(properties, new Authenticator {
            override def getPasswordAuthentication: PasswordAuthentication = {
              return new PasswordAuthentication(emailServer.getUsername, password)
            }
          })
          val mimeMessage = new MimeMessage(session) {
            setSubject(subject)
            setText(message)
            setFrom(new InternetAddress(emailServer.getEmailAddress, emailServer.getFullName))
            addRecipients(RecipientType.TO, emailAdresses.map {
              new InternetAddress(_).asInstanceOf[Address]
            }.toArray)
            saveChanges
          }
          val transport = session.getTransport("smtp")

          transport.connect(emailServer.getHostname, emailServer.getUsername, password)
          transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients)
          transport.close

          ISOptionPane.showMessageDialog(LocaleManager.getString("emailSendingSuccessful"))
        } catch {
          case ex: Exception => {
            ISOptionPane.showMessageDialog(LocaleManager.getString("errorWhileSendingEmail"))
            LoggerManager.getInstance.warn(getClass, ex)
          }
        }
      }
      case _ =>
    }

  }
}
