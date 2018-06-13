/* BSD 2-Clause License:
 * Copyright (c) 2009 - 2017
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
package org.opalj
package fpcf
package properties

import org.opalj.br.DeclaredMethod

sealed trait VirtualMethodPurityPropertyMetaInformation extends PropertyMetaInformation {

    final type Self = VirtualMethodPurity

}

/**
 * Describes the aggregated purity for a virtual method.
 *
 * @author Dominik Helm
 */
sealed case class VirtualMethodPurity(
        individualProperty: Purity
) extends AggregatedProperty[Purity, VirtualMethodPurity]
    with VirtualMethodPurityPropertyMetaInformation {

    /**
     * The globally unique key of the [[VirtualMethodPurity]] property.
     */
    final def key: PropertyKey[VirtualMethodPurity] = VirtualMethodPurity.key

    override def toString: String = s"VirtualMethodPurity($individualProperty)"
}

object VirtualMethodPurity extends VirtualMethodPurityPropertyMetaInformation {

    def apply(name: String): Option[VirtualMethodPurity] =
        if (name.charAt(0) == 'V') Purity(name.substring(1)).map(_.aggregatedProperty) else None

    final val VCompileTimePure = CompileTimePure.aggregatedProperty
    final val VPure = Pure.aggregatedProperty
    final val VSideEffectFree = SideEffectFree.aggregatedProperty
    final val VExternallyPure = ExternallyPure.aggregatedProperty
    final val VExternallySideEffectFree = ExternallySideEffectFree.aggregatedProperty
    final val VContextuallyPure = ContextuallyPure.aggregatedProperty
    final val VContextuallySideEffectFree = ContextuallySideEffectFree.aggregatedProperty
    final val VDPure = DPure.aggregatedProperty
    final val VDSideEffectFree = DSideEffectFree.aggregatedProperty
    final val VDExternallyPure = DExternallyPure.aggregatedProperty
    final val VDExternallySideEffectFree = DExternallySideEffectFree.aggregatedProperty
    final val VDContextuallyPure = DContextuallyPure.aggregatedProperty
    final val VDContextuallySideEffectFree = DContextuallySideEffectFree.aggregatedProperty
    final val VImpureByAnalysis = ImpureByAnalysis.aggregatedProperty
    final val VImpureByLackOfInformation = ImpureByLackOfInformation.aggregatedProperty

    /**
     * The key associated with every purity property. The name is "VirtualMethodPurity";
     * the fallback is "VImpure".
     */
    final val key = PropertyKey.create[DeclaredMethod, VirtualMethodPurity](
        "VirtualMethodPurity",
        VImpureByLackOfInformation
    )
}
