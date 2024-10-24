package org.opalj.Commandline_base.commandlines

object LibraryDirectoryCommand extends OpalPlainCommand[String] {
    override var name: String = "libDir"
    override var argName: String = "libDir"
    override var description: String = "directory with library class files relative to ClassPath"
    override var defaultValue: Option[String] = None
    override var noshort: Boolean = true

    override def parse[T](arg: T): Any = {
        val libraryDir = arg.asInstanceOf[String]
        Some(libraryDir)
    }
}
