package com.externalalarmclock.pojo

import com.openpojo.reflection.filters.FilterPackageInfo
import com.openpojo.reflection.impl.PojoClassFactory
import com.openpojo.validation.ValidatorBuilder
import com.openpojo.validation.affirm.Affirm
import com.openpojo.validation.rule.impl.GetterMustExistRule
import com.openpojo.validation.rule.impl.SetterMustExistRule
import com.openpojo.validation.test.impl.GetterTester
import com.openpojo.validation.test.impl.SetterTester
import org.junit.jupiter.api.Test

class PojoTest {
    @Test
    fun ensureExpectedPojoCount() {
        val pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE, FilterPackageInfo()).filter { it.sourcePath
            .contains("kotlin/main") }
        Affirm.affirmEquals("Classes added / removed? ${pojoClasses.map { it.clazz }}", EXPECTED_CLASS_COUNT, pojoClasses
            .size)
    }

    @Test
    fun testPojoStructureAndBehavior() {
        val validator = ValidatorBuilder.create()
                // Add Rules to validate structure for POJO_PACKAGE
                // See com.openpojo.validation.rule.impl for more ...
                .with(GetterMustExistRule()).with(SetterMustExistRule())
                // Add Testers to validate behaviour for POJO_PACKAGE
                // See com.openpojo.validation.test.impl for more ...
                .with(SetterTester()).with(GetterTester()).build()

        validator.validate(POJO_PACKAGE, FilterPackageInfo())
    }

    companion object {
        // Configured for expectation, so we know when a class gets added or removed.
        private const val EXPECTED_CLASS_COUNT = 2

        // The package to test
        private const val POJO_PACKAGE = "com.externalalarmclock.pojo"
    }
}
