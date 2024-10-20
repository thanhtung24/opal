package org.opalj.Commandline_base.commandlines

import commandlinebase.OpalChoiceCommand

object EscapeCommand extends OpalChoiceCommand{
    override var name: String = "escape"
    override var argName: String = "escape"
    override var description: String = "<none|L0|L1> (Default: L1, the most precise configuration)"
    override var defaultValue: Some[String] = Some("L1")
    override var noshort: Boolean = true
    override var choices: Seq[String] = Seq("none", "L0", "L1")
}
