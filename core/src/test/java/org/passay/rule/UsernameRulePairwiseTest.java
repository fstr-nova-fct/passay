/* Pairwise testing for UsernameRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Black-box tests for {@link UsernameRule#validate(org.passay.PasswordData)}.
 *
 * <p>Implements test battery from pairwise (all-pairs) combinatorial testing
 * (section 3.1.2). Configuration: username "admin", factors matchBackwards,
 * ignoreCase, matchBehavior, and usernameRelation. 9 tests covering all
 * 37 pairs from 4 factors (exhaustive: 36).</p>
 */
public class UsernameRulePairwiseTest {

    /** PW-1 — backwards=true, ignoreCase=true, StartsWith, forward match. */
    @Test
    void testPW1() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "adminSecure123");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "PW-1: password starts with username -> should fail");
    }

    /** PW-2 — backwards=true, ignoreCase=false, EndsWith, reversed match. */
    @Test
    void testPW2() {
        UsernameRule rule = new UsernameRule(true, false, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "Securenimda");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "PW-2: password ends with reversed username -> should fail");
    }

    /** PW-3 — backwards=false, ignoreCase=true, EndsWith, no match. */
    @Test
    void testPW3() {
        UsernameRule rule = new UsernameRule(false, true, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "MyStr0ngPwd!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "PW-3: password does not contain username -> should pass");
    }

    /** PW-4 — backwards=false, ignoreCase=false, Contains, forward match. */
    @Test
    void testPW4() {
        UsernameRule rule = new UsernameRule(false, false, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "theadminpwd");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "PW-4: password contains username -> should fail");
    }

    /** PW-5 — backwards=true, ignoreCase=true, Contains, reversed match. */
    @Test
    void testPW5() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "xyznimdaQRS");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "PW-5: password contains reversed username -> should fail");
    }

    /** PW-6 — backwards=true, ignoreCase=false, StartsWith, no match. */
    @Test
    void testPW6() {
        UsernameRule rule = new UsernameRule(true, false, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "Str0ngP@sswd");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "PW-6: password does not start with username or reversed -> should pass");
    }

    /** PW-7 — backwards=false, ignoreCase=true, StartsWith, reversed match (not checked). */
    @Test
    void testPW7() {
        UsernameRule rule = new UsernameRule(false, true, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "nimdaSecure");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "PW-7: reversed username present but matchBackwards=false -> should pass");
    }

    /** PW-8 — backwards=true, ignoreCase=true, EndsWith, forward match. */
    @Test
    void testPW8() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "Secureadmin");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
                "PW-8: password ends with username -> should fail");
    }

    /** PW-9 — backwards=true, ignoreCase=true, Contains, no match. */
    @Test
    void testPW9() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "Str0ngP@ss!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
                "PW-9: password contains neither username nor reversed -> should pass");
    }
}
