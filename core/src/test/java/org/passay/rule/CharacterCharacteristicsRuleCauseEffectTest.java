/* Cause-Effect Graphing tests for CharacterCharacteristicsRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.data.EnglishCharacterData;

/**
 * Black-box tests for {@link CharacterCharacteristicsRule#validate(org.passay.PasswordData)}.
 *
 * <p>Implements test battery from cause-effect graphing and decision table
 * (section 3.1.3). Configuration: 3 character rules (uppercase, digit,
 * special), numCharacteristics = 2. Full decision table: 2^3 = 8 test
 * cases.</p>
 */
public class CharacterCharacteristicsRuleCauseEffectTest {

    /** Creates the rule: 3 CharacterRules, at least 2 must pass. */
    private CharacterCharacteristicsRule createRule() {
        return new CharacterCharacteristicsRule(
                2,
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.SpecialAscii, 1)
        );
    }

    /** DT1 — C1=F, C2=F, C3=F -> count=0 -> FAIL. */
    @Test
    void testDT1_noCausesTrue() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "DT1: 0 of 3 causes true (count=0) -> should fail");
    }

    /** DT2 — C1=F, C2=F, C3=T -> count=1 -> FAIL. */
    @Test
    void testDT2_onlySpecial() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("abcdef!");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "DT2: 1 of 3 causes true (only C3, count=1) -> should fail");
    }

    /** DT3 — C1=F, C2=T, C3=F -> count=1 -> FAIL. */
    @Test
    void testDT3_onlyDigit() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("abcdef1");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "DT3: 1 of 3 causes true (only C2, count=1) -> should fail");
    }

    /** DT4 — C1=F, C2=T, C3=T -> count=2 -> SUCCESS. */
    @Test
    void testDT4_digitAndSpecial() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("abcde1!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "DT4: 2 of 3 causes true (C2+C3, count=2) -> should pass");
    }

    /** DT5 — C1=T, C2=F, C3=F -> count=1 -> FAIL. */
    @Test
    void testDT5_onlyUppercase() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("Abcdef");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "DT5: 1 of 3 causes true (only C1, count=1) -> should fail");
    }

    /** DT6 — C1=T, C2=F, C3=T -> count=2 -> SUCCESS. */
    @Test
    void testDT6_uppercaseAndSpecial() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("Abcdef!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "DT6: 2 of 3 causes true (C1+C3, count=2) -> should pass");
    }

    /** DT7 — C1=T, C2=T, C3=F -> count=2 -> SUCCESS. */
    @Test
    void testDT7_uppercaseAndDigit() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("Abcdef1");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "DT7: 2 of 3 causes true (C1+C2, count=2) -> should pass");
    }

    /** DT8 — C1=T, C2=T, C3=T -> count=3 -> SUCCESS. */
    @Test
    void testDT8_allCausesTrue() {
        CharacterCharacteristicsRule rule = createRule();
        PasswordData pd = new PasswordData("Abcde1!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "DT8: 3 of 3 causes true (all, count=3) -> should pass");
    }
}
