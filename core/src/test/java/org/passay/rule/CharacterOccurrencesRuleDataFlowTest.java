/* Data Flow tests for CharacterOccurrencesRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * White-box tests for {@link CharacterOccurrencesRule#validate(PasswordData)}.
 *
 * <p>Implements test batteries from data flow analysis (section 3.3).
 * Covers all-definitions, all-uses, and all-def-uses criteria.
 * Variables tracked: details, password, codePoints, repeat, i.</p>
 */
public class CharacterOccurrencesRuleDataFlowTest {

    /* 1. All-Definitions — at least one use reached per definition. */

    /** AD1 — all different chars, no violation -> SUCCESS. */
    @Test
    void testAD1_allDifferent() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "AD1: no char exceeds maxOcc=2 -> should pass");
    }

    /** AD2 — triple char exceeds maxOcc=2, details.add triggered -> FAIL. */
    @Test
    void testAD2_tripleCharFails() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabcd");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "AD2: 'aaa' exceeds maxOcc=2 -> should fail");
    }

    /* 2. All-Uses — every DU pair exercised via at least one def-clear path. */

    /** AU1 — all different chars, covers def(details S2)->use(S15), def(repeat S6)->use(S11:F). */
    @Test
    void testAU1_noDuplicates() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "AU1: details remains empty -> should pass");
    }

    /** AU2 — pair at maxOcc boundary, covers def(repeat S6)->use(S10), def(repeat S10)->use(S11:F). */
    @Test
    void testAU2_pairAtBoundary() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aabcde");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "AU2: 'aa' repeat=2, not > 2 -> should pass");
    }

    /** AU3 — triple char, covers def(repeat S10)->use(S12), def(details S2)->use(S12), def(repeat S13)->use(S11). */
    @Test
    void testAU3_tripleExceedsBoundary() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabcd");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "AU3: 'aaa' repeat=3 > 2 -> should fail");
    }

    /** AU4 — two violations, covers def(repeat S13)->use(S10) across second group. */
    @Test
    void testAU4_twoViolations() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabbb");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "AU4: 'aaa' and 'bbb' both exceed maxOcc=2 -> should fail");
        assertTrue(result.getDetails().size() >= 2,
            "AU4: should report 2 violations");
    }

    /** AU5 — minimal password, covers def(i S7)->use(S8), def(i S7)->use(S9). */
    @Test
    void testAU5_minimalPassword() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("ab");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "AU5: minimal 2-char password, no repeats -> should pass");
    }

    /** AU6 — single repeated char, covers def(repeat S10)->use(S10) self-loop, def(i S14)->use(S14). */
    @Test
    void testAU6_singleCharRepeated() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaa");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "AU6: 'aaa' repeat=3 > 2 -> should fail");
    }

    /* 3. All-Def-Uses — every def-clear path for every DU pair. */

    /** ADU1 — all different, exercises S9(F)->S11(F)->S13 path for repeat. */
    @Test
    void testADU1_allDifferentChars() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "ADU1: repeat never exceeds 1 -> should pass");
    }

    /** ADU2 — pair then different, exercises S9(T)->S10 then S9(F)->S11(F) path. */
    @Test
    void testADU2_pairThenDifferent() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aabcde");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "ADU2: 'aa' at boundary, not exceeded -> should pass");
    }

    /** ADU3 — triple then different, exercises S10->S14->S8->S9(F)->S11(T)->S12 path. */
    @Test
    void testADU3_tripleExceeds() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabcd");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "ADU3: 'aaa' -> details.add called -> should fail");
    }

    /** ADU4 — two groups, exercises def(repeat S13)->use(S10) across group boundary. */
    @Test
    void testADU4_twoGroupsBothExceed() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabbb");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "ADU4: two violations -> should fail");
    }

    /** ADU5 — pair then triple, exercises def(repeat S13)->use(S10) where second group exceeds. */
    @Test
    void testADU5_pairThenTriple() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aabbbc");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "ADU5: 'bbb' exceeds maxOcc after 'aa' resets repeat -> should fail");
    }

    /** ADU6 — triple then pair, exercises def(repeat S13)->use(S11:F) where second group does not exceed. */
    @Test
    void testADU6_tripleThenPair() {
        CharacterOccurrencesRule rule = new CharacterOccurrencesRule(2);
        PasswordData pd = new PasswordData("aaabb");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "ADU6: 'aaa' exceeds, 'bb' does not -> should fail (1 violation)");
    }
}
