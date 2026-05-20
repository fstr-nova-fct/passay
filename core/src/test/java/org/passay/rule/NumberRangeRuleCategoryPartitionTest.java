/* Category Partition tests for NumberRangeRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Black-box tests for {@link NumberRangeRule#validate(org.passay.PasswordData)}.
 *
 * <p>Implements a sample of the category-partition battery from TSL
 * (section 3.1.4). Specification: {@code core/src/test/resources/NumberRangeRule.tsl}
 * (51 frames). Configuration: lowerRange = 10, upperRange = 20.
 * 15 representative frames implemented in JUnit.</p>
 */
public class NumberRangeRuleCategoryPartitionTest {

    /** TF-1 — no number from range, Contains. */
    @Test
    void testTF1_noNumberFromRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abcdefgh");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "TF-1: no number from range in password -> should pass");
    }

    /** TF-2 — boundary 10, at start, Contains, reportAll=true. */
    @Test
    void testTF2_boundary10_atStart_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-2: boundary 10 at start, Contains -> should fail");
    }

    /** TF-4 — boundary 10, at start, StartsWith. */
    @Test
    void testTF4_boundary10_atStart_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-4: boundary 10 at start, StartsWith -> should fail");
    }

    /** TF-5 — boundary 10, at start, EndsWith. */
    @Test
    void testTF5_boundary10_atStart_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "TF-5: 10 at start but EndsWith -> no match -> should pass");
    }

    /** TF-6 — boundary 10, in middle, Contains, reportAll=true. */
    @Test
    void testTF6_boundary10_inMiddle_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc10def");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-6: boundary 10 in middle, Contains -> should fail");
    }

    /** TF-13 — boundary 10, at end, EndsWith. */
    @Test
    void testTF13_boundary10_atEnd_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("abcdef10");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-13: boundary 10 at end, EndsWith -> should fail");
    }

    /** TF-22 — boundary 19, at end, Contains. */
    @Test
    void testTF22_boundary19_atEnd_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abcdef19");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-22: boundary 19 at end, Contains -> should fail");
    }

    /** TF-30 — mid-range 15, in middle, Contains, reportAll=true. */
    @Test
    void testTF30_midRange15_inMiddle_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc15def");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-30: mid-range 15 in middle, Contains -> should fail");
    }

    /** TF-32 — mid-range 15, in middle, StartsWith. */
    @Test
    void testTF32_midRange15_inMiddle_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("abc15def");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "TF-32: 15 in middle but StartsWith -> no match -> should pass");
    }

    /** TF-38 — number below range (9), Contains. */
    @Test
    void testTF38_numberBelowRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc9defg");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "TF-38: number 9 is below range [10,20) -> should pass");
    }

    /** TF-39 — number above range (20), Contains. */
    @Test
    void testTF39_numberAboveRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc20def");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "TF-39: number 20 is above range [10,20) -> should pass");
    }

    /** TF-40 — multiple numbers (12, 17), Contains, reportAll=true. */
    @Test
    void testTF40_multipleNumbers_contains_reportAll() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("12abc17d");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-40: multiple numbers from range, Contains, reportAll -> should fail");
        assertEquals(2, result.getDetails().size(),
                "TF-40: reportAll=true should report exactly two failures (12 and 17)");
    }

    /** TF-41 — multiple numbers (12, 17), Contains, reportAll=false. */
    @Test
    void testTF41_multipleNumbers_contains_reportFirst() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, false);
        PasswordData pd = new PasswordData("12abc17d");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-41: multiple numbers from range, Contains, reportFirst -> should fail");
        assertEquals(1, result.getDetails().size(),
                "TF-41: reportAll=false should report only first failure");
    }

    /** TF-28 — mid-range 15, at start, StartsWith. */
    @Test
    void testTF28_midRange15_atStart_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("15abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-28: 15 at start, StartsWith -> should fail");
    }

    /** TF-37 — mid-range 15, at end, EndsWith. */
    @Test
    void testTF37_midRange15_atEnd_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("abcdef15");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "TF-37: 15 at end, EndsWith -> should fail");
    }
}