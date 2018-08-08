/* BSD 2-Clause License - see OPAL/LICENSE for details. */
package org.opalj
package br
package reader

import scala.reflect.ClassTag

import org.opalj.bi.reader.InnerClasses_attributeReader

/**
 * The factory methods to create inner classes attributes and entries.
 *
 * @author Michael Eichberg
 */
trait InnerClasses_attributeBinding
    extends InnerClasses_attributeReader
    with ConstantPoolBinding
    with AttributeBinding {

    type InnerClasses_attribute = br.InnerClassTable
    type InnerClassesEntry = br.InnerClass
    val InnerClassesEntryManifest: ClassTag[InnerClassesEntry] = implicitly

    def InnerClasses_attribute(
        cp:                   Constant_Pool,
        attribute_name_index: Constant_Pool_Index,
        inner_classes:        InnerClasses,
        // The scope in which the attribute is defined
        as_name_index:       Constant_Pool_Index,
        as_descriptor_index: Constant_Pool_Index
    ): InnerClasses_attribute =
        new InnerClasses_attribute(inner_classes)

    def InnerClassesEntry(
        cp:                       Constant_Pool,
        inner_class_info_index:   Constant_Pool_Index,
        outer_class_info_index:   Constant_Pool_Index,
        inner_name_index:         Constant_Pool_Index,
        inner_class_access_flags: Int
    ): InnerClassesEntry = {
        new InnerClassesEntry(
            cp(inner_class_info_index).asObjectType(cp),
            if (outer_class_info_index == 0)
                None
            else
                Some(cp(outer_class_info_index).asObjectType(cp)),
            if (inner_name_index == 0)
                None
            else
                Some(cp(inner_name_index).asString),
            inner_class_access_flags
        )
    }
}

