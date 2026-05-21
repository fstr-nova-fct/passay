/* Control Flow tests for RepeatCharactersRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * White-box tests for {@link RepeatCharactersRule#validate(PasswordData)}.
 *
 * <p>Implements test batteries from control flow analysis (section 3.2.1).
 * Covers statement, condition, decision, MC/DC, and independent path coverage.
 * Cyclomatic complexity V(G) = 5, with 4 decisions and 14 nodes.</p>
 */
public class RepeatCharactersRuleControlFlowTest {

    /* 1. Statement Coverage — every node N1-N14 executed. */

    /** SC1 — all different chars, no repeat found -> SUCCESS. */
    @Test
    void testSC1_noRepeats() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "SC1: no repeating sequences -> should pass");
    }

    /** SC2 — two sequences "aa" and "bb", count=2 >= seqCount=2 -> FAIL. */
    @Test
    void testSC2_twoSequencesFound() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aabbc");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "SC2: 2 sequences of 2 repeats -> should fail");
    }

    /* 2. Decision Coverage — each decision True and False. */

    /** DC1 — D1:T/F, D2:F, D3:F, D4:F -> SUCCESS. */
    @Test
    void testDC1_allDecisionsFalse() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "DC1: D2=F, D3=F, D4=F -> should pass");
    }

    /** DC2 — D1:T/F, D2:T, D3:T, D4:T -> FAIL. */
    @Test
    void testDC2_allDecisionsTrue() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aabbc");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "DC2: D2=T, D3=T, D4=T -> should fail");
    }

    /* 3. Condition Coverage — each atomic condition True and False. */

    /** CC1 — all conditions false -> SUCCESS. */
    @Test
    void testCC1_allConditionsFalse() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "CC1: all conditions false -> should pass");
    }

    /** CC2 — all conditions true -> FAIL. */
    @Test
    void testCC2_allConditionsTrue() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aabbc");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "CC2: all conditions true -> should fail");
    }

    /* 4. MC/DC Coverage — each condition independently affects outcome. */

    /** MCDC1 — D2:F, D3:F, D4:F -> SUCCESS. */
    @Test
    void testMCDC1_noRepeats() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("abcdef");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "MCDC1: no repeats at all -> should pass");
    }

    /** MCDC2 — D2:T, D3:T, D4:T -> FAIL. */
    @Test
    void testMCDC2_twoSequencesFail() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aabbc");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "MCDC2: 2 sequences found, count >= seqCount -> should fail");
    }

    /** MCDC3 — D2:T, D3:T, D4:F (flips D4 independently) -> SUCCESS. */
    @Test
    void testMCDC3_oneSequenceButNeedTwo() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aab");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "MCDC3: 1 sequence found but seqCount=2 -> should pass");
    }

    /* 5. Independent Path Coverage — V(G) = 5 independent paths. */

    /** IP1 — Path 1: empty password, loop processes only sentinel -> SUCCESS. */
    @Test
    void testIP1_emptyPassword() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "IP1: empty password -> should pass");
    }

    /** IP2 — Path 2: all different chars, D2 always false -> SUCCESS. */
    @Test
    void testIP2_allDifferentChars() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("ab");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "IP2: all different, no repeats -> should pass");
    }

    /** IP3 — Path 3: repeat found but shorter than seqLen, D3 false -> SUCCESS. */
    @Test
    void testIP3_repeatTooShort() {
        RepeatCharactersRule rule = new RepeatCharactersRule(3, 1);
        PasswordData pd = new PasswordData("aa");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "IP3: repeat=2 but seqLen=3 -> should pass");
    }

    /** IP4 — Path 4: one sequence found but count < seqCount, D4 false -> SUCCESS. */
    @Test
    void testIP4_sequenceFoundButNotEnough() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 2);
        PasswordData pd = new PasswordData("aab");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "IP4: 1 sequence found but need 2 -> should pass");
    }

    /** IP5 — Path 5: one sequence found and count >= seqCount, D4 true -> FAIL. */
    @Test
    void testIP5_sequenceCountReached() {
        RepeatCharactersRule rule = new RepeatCharactersRule(2, 1);
        PasswordData pd = new PasswordData("aab");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "IP5: 1 sequence found and seqCount=1 -> should fail");
    }
}
