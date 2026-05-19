/* Category Partition tests for NumberRangeRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Category Partition test suite for {@link NumberRangeRule#validate}.
 *
 * Configuration: lowerRange=10, upperRange=20 (checks numbers 10 through 19)
 *
 * Parameters and Categories:
 *   P1: Password number content
 *       - no number from range / boundary 10 / boundary 19 / mid-range 15
 *       - number below range (9) / above range (20) / multiple numbers
 *   P2: Number position: at start / in middle / at end
 *   P3: Match behavior: Contains / StartsWith / EndsWith
 *   P4: Report all failures: true / false / indifferent
 *
 * Full specification: 51 test frames generated (see .tsl file)
 * Representative subset: 15 test cases implemented below
 */
public class NumberRangeRuleCategoryPartitionTest {

    // ---------------------------------------------------------------
    // TF-1: No number from range, Contains
    // Password "abcdefgh" has no numbers at all -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testTF1_noNumberFromRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abcdefgh");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "TF-1: no number from range in password -> should pass");
    }

    // ---------------------------------------------------------------
    // TF-2: Boundary number 10, at start, Contains, reportAll=true
    // Password "10abcdef" contains 10 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF2_boundary10_atStart_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-2: boundary 10 at start, Contains -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-4: Boundary number 10, at start, StartsWith
    // Password "10abcdef" starts with 10 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF4_boundary10_atStart_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-4: boundary 10 at start, StartsWith -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-5: Boundary number 10, at start, EndsWith
    // Password "10abcdef" does NOT end with any number 10-19 -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testTF5_boundary10_atStart_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("10abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "TF-5: 10 at start but EndsWith -> no match -> should pass");
    }

    // ---------------------------------------------------------------
    // TF-6: Boundary number 10, in middle, Contains, reportAll=true
    // Password "abc10def" contains 10 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF6_boundary10_inMiddle_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc10def");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-6: boundary 10 in middle, Contains -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-13: Boundary number 10, at end, EndsWith
    // Password "abcdef10" ends with 10 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF13_boundary10_atEnd_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("abcdef10");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-13: boundary 10 at end, EndsWith -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-22: Boundary number 19, at end, Contains
    // Password "abcdef19" contains 19 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF22_boundary19_atEnd_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abcdef19");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-22: boundary 19 at end, Contains -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-30: Mid-range number 15, in middle, Contains, reportAll=true
    // Password "abc15def" contains 15 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF30_midRange15_inMiddle_contains() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc15def");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-30: mid-range 15 in middle, Contains -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-32: Mid-range number 15, in middle, StartsWith
    // Password "abc15def" does NOT start with any number 10-19 -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testTF32_midRange15_inMiddle_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("abc15def");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "TF-32: 15 in middle but StartsWith -> no match -> should pass");
    }

    // ---------------------------------------------------------------
    // TF-38: Number below range (9), Contains
    // Password "abc9defg" contains 9 but 9 is not in [10,20) -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testTF38_numberBelowRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc9defg");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "TF-38: number 9 is below range [10,20) -> should pass");
    }

    // ---------------------------------------------------------------
    // TF-39: Number above range (20), Contains
    // Password "abc20def" contains 20 but 20 is not in [10,20) -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testTF39_numberAboveRange() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("abc20def");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "TF-39: number 20 is above range [10,20) -> should pass");
    }

    // ---------------------------------------------------------------
    // TF-40: Multiple numbers (12 and 17), at start, Contains, reportAll=true
    // Password "12abc17d" contains both 12 and 17 -> FAIL (reports both)
    // ---------------------------------------------------------------
    @Test
    void testTF40_multipleNumbers_contains_reportAll() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, true);
        PasswordData pd = new PasswordData("12abc17d");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-40: multiple numbers from range, Contains, reportAll -> should fail");
        // With reportAll=true, should report both 12 and 17
        assertTrue(result.getDetails().size() >= 2,
            "TF-40: reportAll=true should report multiple failures");
    }

    // ---------------------------------------------------------------
    // TF-41: Multiple numbers (12 and 17), at start, Contains, reportAll=false
    // Password "12abc17d" contains both but only first reported -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF41_multipleNumbers_contains_reportFirst() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.Contains, false);
        PasswordData pd = new PasswordData("12abc17d");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-41: multiple numbers from range, Contains, reportFirst -> should fail");
        // With reportAll=false, should report only first match
        assertEquals(1, result.getDetails().size(),
            "TF-41: reportAll=false should report only first failure");
    }

    // ---------------------------------------------------------------
    // TF-28: Mid-range 15, at start, StartsWith
    // Password "15abcdef" starts with 15 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF28_midRange15_atStart_startsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.StartsWith, true);
        PasswordData pd = new PasswordData("15abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-28: 15 at start, StartsWith -> should fail");
    }

    // ---------------------------------------------------------------
    // TF-37: Mid-range 15, at end, EndsWith
    // Password "abcdef15" ends with 15 -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testTF37_midRange15_atEnd_endsWith() {
        NumberRangeRule rule = new NumberRangeRule(10, 20, MatchBehavior.EndsWith, true);
        PasswordData pd = new PasswordData("abcdef15");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "TF-37: 15 at end, EndsWith -> should fail");
    }
}
