/* BSD 2-Clause License:
 * Copyright (c) 2009 - 2014
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
package tac

import org.scalatest.Matchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.junit.runner.RunWith

import org.opalj.br._
import org.opalj.bi.TestSupport.locateTestResources
import org.opalj.br.analyses.Project
import org.opalj.ai.BaseAI
import org.opalj.ai.domain.l1.DefaultDomain

/**
 * Tests the conversion of parsed methods to a quadruple representation
 *
 * @author Michael Eichberg
 * @author Roberts Kolosovs
 */
@RunWith(classOf[JUnitRunner])
class ArrayTest extends FunSpec with Matchers {

  val ArrayInstructionsType = ObjectType("tactest/ArrayCreationAndManipulation")

  val testResources = locateTestResources("classfiles/tactest.jar", "ai")

  val project = Project(testResources)

  val ArrayInstructionsClassFile = project.classFile(ArrayInstructionsType).get

  val RefArrayMethod = ArrayInstructionsClassFile.findMethod("refArray").get
  val MultidimArrayMethod = ArrayInstructionsClassFile.findMethod("multidimArray").get
  val DoubleArrayMethod = ArrayInstructionsClassFile.findMethod("doubleArray").get
  val FloatArrayMethod = ArrayInstructionsClassFile.findMethod("floatArray").get
  val IntArrayMethod = ArrayInstructionsClassFile.findMethod("intArray").get
  val LongArrayMethod = ArrayInstructionsClassFile.findMethod("longArray").get
  val ShortArrayMethod = ArrayInstructionsClassFile.findMethod("shortArray").get
  val ByteArrayMethod = ArrayInstructionsClassFile.findMethod("byteArray").get
  val CharArrayMethod = ArrayInstructionsClassFile.findMethod("charArray").get

  describe("The quadruples representation of array creation and manipulation instructions") {

    describe("using no AI results") {
      def expectedAST(cTpe: ComputationalType, Tpe: Type, const: Expr) = Array[Stmt](
            Assignment(-1, SimpleVar(-1, ComputationalTypeReference), Param(ComputationalTypeReference, "this")),
            Assignment(0, SimpleVar(0, ComputationalTypeInt), IntConst(0, 5)),
            Assignment(1, SimpleVar(0, ComputationalTypeReference), NewArray(1, SimpleVar(0, ComputationalTypeInt), Tpe)),
            Assignment(3, SimpleVar(-2, ComputationalTypeReference),SimpleVar(0, ComputationalTypeReference)),
            Assignment(4, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(5, SimpleVar(1, ComputationalTypeInt), IntConst(5, 4)),
            Assignment(6, SimpleVar(2, cTpe), const),
            ArrayStore(7, SimpleVar(0, ComputationalTypeReference), SimpleVar(1, ComputationalTypeInt), SimpleVar(2, cTpe)),
            Assignment(8, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(9, SimpleVar(1, ComputationalTypeInt), IntConst(9, 4)),
            Assignment(10, SimpleVar(0, cTpe), ArrayLoad(10, SimpleVar(1, ComputationalTypeInt), SimpleVar(0, ComputationalTypeReference))),
            Assignment(11, SimpleVar(-3, cTpe), SimpleVar(0, cTpe)),
            Return(12))
            
      def expectedJLC(tpe: String, value: String) = Array[String](
            "0: r_0 = this;",
            "1: op_0 = 5;",
            "2: op_0 = new "+tpe+"[op_0];",
            "3: r_1 = op_0;",
            "4: op_0 = r_1;",
            "5: op_1 = 4;",
            "6: op_2 = "+value+";",
            "7: op_0[op_1] = op_2;",
            "8: op_0 = r_1;",
            "9: op_1 = 4;",
            "10: op_0 = op_0[op_1];",
            "11: r_2 = op_0;",
            "12: return;")      
            
      it("should correctly reflect reference array instructions") {
        val statements = AsQuadruples(RefArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(Array())
        javaLikeCode.shouldEqual(Array())
      }

      it("should correctly reflect multidimensional array instructions") {
        val statements = AsQuadruples(MultidimArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(Array(
            Assignment(-1, SimpleVar(-1, ComputationalTypeReference), Param(ComputationalTypeReference, "this")),
            Assignment(0, SimpleVar(0, ComputationalTypeInt), IntConst(0, 4)),
            Assignment(1, SimpleVar(1, ComputationalTypeInt), IntConst(1,2)),
            Assignment(2, SimpleVar(0, ComputationalTypeReference), 
                NewMultiArray(2, List(SimpleVar(1, ComputationalTypeInt), SimpleVar(0, ComputationalTypeInt)), 2, IntegerType)),
            Assignment(6, SimpleVar(-2, ComputationalTypeReference), SimpleVar(0, ComputationalTypeReference)),
            Assignment(7, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(8, SimpleVar(0, ComputationalTypeInt), ArrayLength(8, SimpleVar(0, ComputationalTypeReference))),
            Assignment(9, SimpleVar(-3, ComputationalTypeInt), SimpleVar(0, ComputationalTypeInt)),
            Return(10)
        ))
        javaLikeCode.shouldEqual(Array(
            "0: r_0 = this;",
            "1: op_0 = 4;",
            "2: op_1 = 2;",
            "3: op_0 = new int[op_0][op_1];",
            "4: r_1 = op_0;",
            "5: op_0 = r_1;",
            "6: op_0 = op_0.length;",
            "7: r_2 = op_0;",
            "8: return;"
        ))
      }

      it("should correctly reflect double array instructions") {
        val statements = AsQuadruples(DoubleArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeDouble, DoubleType, DoubleConst(6, 1.0d)))
        javaLikeCode.shouldEqual(expectedJLC("double", "1.0d"))
      }

      it("should correctly reflect float array instructions") {
        val statements = AsQuadruples(FloatArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)
        
        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeFloat, FloatType, FloatConst(6, 2.0f)))
        javaLikeCode.shouldEqual(expectedJLC("float", "2.0"))
      }

      it("should correctly reflect int array instructions") {
        val statements = AsQuadruples(IntArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, IntegerType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("int", "2"))
      }

      it("should correctly reflect long array instructions") {
        val statements = AsQuadruples(LongArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeLong, LongType, LongConst(6, 1)))
        javaLikeCode.shouldEqual(expectedJLC("long", "1l"))
      }

      it("should correctly reflect short array instructions") {
        val statements = AsQuadruples(ShortArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, ShortType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("short", "2"))
      }

      it("should correctly reflect byte array instructions") {
        val statements = AsQuadruples(ByteArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, ByteType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("byte", "2"))
      }

      it("should correctly reflect char array instructions") {
        val statements = AsQuadruples(CharArrayMethod, None)
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, CharType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("char", "2"))
      }
    }

    describe("using AI results") {
      def expectedAST(cTpe: ComputationalType, Tpe: Type, const: Expr) = Array[Stmt](
            Assignment(-1, SimpleVar(-1, ComputationalTypeReference), Param(ComputationalTypeReference, "this")),
            Assignment(0, SimpleVar(0, ComputationalTypeInt), IntConst(0, 5)),
            Assignment(1, SimpleVar(0, ComputationalTypeReference), NewArray(1, SimpleVar(0, ComputationalTypeInt), Tpe)),
            Assignment(3, SimpleVar(-2, ComputationalTypeReference),SimpleVar(0, ComputationalTypeReference)),
            Assignment(4, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(5, SimpleVar(1, ComputationalTypeInt), IntConst(5, 4)),
            Assignment(6, SimpleVar(2, cTpe), const),
            ArrayStore(7, SimpleVar(0, ComputationalTypeReference), SimpleVar(1, ComputationalTypeInt), SimpleVar(2, cTpe)),
            Assignment(8, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(9, SimpleVar(1, ComputationalTypeInt), IntConst(9, 4)),
            Assignment(10, SimpleVar(0, cTpe), ArrayLoad(10, SimpleVar(1, ComputationalTypeInt), SimpleVar(0, ComputationalTypeReference))),
            Assignment(11, SimpleVar(-3, cTpe), SimpleVar(0, cTpe)),
            Return(12))
            
      def expectedJLC(tpe: String, value: String) = Array[String](
            "0: r_0 = this;",
            "1: op_0 = 5;",
            "2: op_0 = new "+tpe+"[op_0];",
            "3: r_1 = op_0;",
            "4: op_0 = r_1;",
            "5: op_1 = 4;",
            "6: op_2 = "+value+";",
            "7: op_0[op_1] = op_2;",
            "8: op_0 = r_1;",
            "9: op_1 = 4;",
            "10: op_0 = op_0[op_1];",
            "11: r_2 = op_0;",
            "12: return;")      
            
      it("should correctly reflect reference array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, RefArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, RefArrayMethod, domain)
        val statements = AsQuadruples(RefArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(Array())
        javaLikeCode.shouldEqual(Array())
      }

      it("should correctly reflect multidimensional array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, MultidimArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, MultidimArrayMethod, domain)
        val statements = AsQuadruples(MultidimArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(Array(
            Assignment(-1, SimpleVar(-1, ComputationalTypeReference), Param(ComputationalTypeReference, "this")),
            Assignment(0, SimpleVar(0, ComputationalTypeInt), IntConst(0, 4)),
            Assignment(1, SimpleVar(1, ComputationalTypeInt), IntConst(1,2)),
            Assignment(2, SimpleVar(0, ComputationalTypeReference), 
                NewMultiArray(2, List(SimpleVar(1, ComputationalTypeInt), SimpleVar(0, ComputationalTypeInt)), 2, IntegerType)),
            Assignment(6, SimpleVar(-2, ComputationalTypeReference), SimpleVar(0, ComputationalTypeReference)),
            Assignment(7, SimpleVar(0, ComputationalTypeReference), SimpleVar(-2, ComputationalTypeReference)),
            Assignment(8, SimpleVar(0, ComputationalTypeInt), ArrayLength(8, SimpleVar(0, ComputationalTypeReference))),
            Assignment(9, SimpleVar(-3, ComputationalTypeInt), SimpleVar(0, ComputationalTypeInt)),
            Return(10)
        ))
        javaLikeCode.shouldEqual(Array(
            "0: r_0 = this;",
            "1: op_0 = 4;",
            "2: op_1 = 2;",
            "3: op_0 = new int[op_0][op_1];",
            "4: r_1 = op_0;",
            "5: op_0 = r_1;",
            "6: op_0 = op_0.length;",
            "7: r_2 = op_0;",
            "8: return;"
        ))
      }

      it("should correctly reflect double array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, DoubleArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, DoubleArrayMethod, domain)
        val statements = AsQuadruples(DoubleArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeDouble, DoubleType, DoubleConst(6, 1.0d)))
        javaLikeCode.shouldEqual(expectedJLC("double", "1.0d"))
      }

      it("should correctly reflect float array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, FloatArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, FloatArrayMethod, domain)
        val statements = AsQuadruples(FloatArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)
        
        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeFloat, FloatType, FloatConst(6, 2.0f)))
        javaLikeCode.shouldEqual(expectedJLC("float", "2.0"))
      }

      it("should correctly reflect int array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, IntArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, IntArrayMethod, domain)
        val statements = AsQuadruples(IntArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, IntegerType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("int", "2"))
      }

      it("should correctly reflect long array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, LongArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, LongArrayMethod, domain)
        val statements = AsQuadruples(LongArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeLong, LongType, LongConst(6, 1)))
        javaLikeCode.shouldEqual(expectedJLC("long", "1l"))
      }

      it("should correctly reflect short array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, ShortArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, ShortArrayMethod, domain)
        val statements = AsQuadruples(ShortArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, ShortType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("short", "2"))
      }

      it("should correctly reflect byte array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, ByteArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, ByteArrayMethod, domain)
        val statements = AsQuadruples(ByteArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, ByteType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("byte", "2"))
      }

      it("should correctly reflect char array instructions") {
        val domain = new DefaultDomain(project, ArrayInstructionsClassFile, CharArrayMethod)
        val aiResult = BaseAI(ArrayInstructionsClassFile, CharArrayMethod, domain)
        val statements = AsQuadruples(CharArrayMethod, Some(aiResult))
        val javaLikeCode = ToJavaLike(statements, false)

        assert(statements.nonEmpty)
        assert(javaLikeCode.length > 0)
        statements.shouldEqual(expectedAST(ComputationalTypeInt, CharType, IntConst(6, 2)))
        javaLikeCode.shouldEqual(expectedJLC("char", "2"))
      }
    }
  }
}