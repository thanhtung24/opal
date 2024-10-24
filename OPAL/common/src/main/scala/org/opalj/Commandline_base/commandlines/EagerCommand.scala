package org.opalj.Commandline_base.commandlines

object EagerCommand extends OpalPlainCommand[Boolean] {
    override var name: String = "eager"
    override var argName: String = "eager"
    override var description: String = "supporting analyses are executed eagerly"
    override var defaultValue: Option[Boolean] = Some(false)
    override var noshort: Boolean = true

    override def parse[T](arg: T): Any = null
}
