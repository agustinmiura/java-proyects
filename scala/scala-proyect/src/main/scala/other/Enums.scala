package ar.com.name
package other

import other.Enums.{PermissionWithBits, Permissions}

object Enums {

  enum Permissions {
    case READ, WRITE, EXECUTE, NONE

    def openDocument(): Unit =
      if (this == READ) println("opening document ...")
      else println("reading not allowed")

  }

  enum PermissionWithBits(bits: Int) {
    case READ extends PermissionWithBits(4)
    case WRITE extends PermissionWithBits(2)
    case EXECUTE extends PermissionWithBits(1)
    case NONE extends PermissionWithBits(0)
  }

  object PermissionWithBits {
    def fromBits(bits: Int): PermissionWithBits =
      PermissionWithBits.NONE
  }

  val somePermissions: Permissions = Permissions.READ

  val somePermissionsOrdinal = somePermissions.ordinal

  val allPermissions = PermissionWithBits.values

  val readPermissions = Permissions.valueOf("READ")

  def main(args: Array[String]): Unit = {
    somePermissions.openDocument()
    println(readPermissions)
  }

}
