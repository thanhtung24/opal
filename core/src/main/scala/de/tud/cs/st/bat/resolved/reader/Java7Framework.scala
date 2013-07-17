/* License (BSD Style License):
 * Copyright (c) 2009 - 2013
 * Software Technology Group
 * Department of Computer Science
 * Technische Universität Darmstadt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  - Neither the name of the Software Technology Group or Technische
 *    Universität Darmstadt nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.tud.cs.st
package bat
package resolved
package reader

import bat.reader.InterfacesReader
import bat.reader.FieldsReader
import bat.reader.MethodsReader
import bat.reader.AttributesReader
import bat.reader.SkipUnknown_attributeReader
import bat.reader.CodeReader

/**
 * This "framework" can be used to read in Java 7 (version 51) class files. All
 * standard information (as defined in the Java Virtual Machine Specification)
 * is represented.
 *
 * @author Michael Eichberg
 */
class Java7Framework
    extends ConstantPoolBinding
    with ClassFileBinding
    with InterfacesReader
    with FieldsReader
    with MethodsReader
    with AttributesReader
    with BootstrapMethods_attributeBinding
    /* If you want unknown attributes to be represented uncomment the following: */
    // with Unknown_attributeBinding 
    /* and comment out the following line: */
    with SkipUnknown_attributeReader
    with AnnotationsBinding
    with StackMapTable_attributeBinding
    with InnerClasses_attributeBinding
    with EnclosingMethod_attributeBinding
    with SourceFile_attributeBinding
    with SourceDebugExtension_attributeBinding
    with Deprecated_attributeBinding
    with Signature_attributeBinding
    with Synthetic_attributeBinding
    with LineNumberTable_attributeBinding
    with LocalVariableTable_attributeBinding
    with LocalVariableTypeTable_attributeBinding
    with Exceptions_attributeBinding
    with ConstantValue_attributeBinding
    with BytecodeReaderAndBinding
    with CodeAttributeBinding
    with CodeReader

object Java7Framework extends Java7Framework


