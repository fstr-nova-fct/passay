/* Pairwise testing for UsernameRule.validate() */
package org.passay.rule;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Pairwise test suite for {@link UsernameRule#validate}.
 *
 * Factors:
 *   F1: matchBackwards  {true, false}
 *   F2: ignoreCase      {true, false}
 *   F3: matchBehavior   {StartsWith, EndsWith, Contains}
 *   F4: usernameRelation {forward_match, reversed_match, no_match}
 *
 * Exhaustive: 2 x 2 x 3 x 3 = 36 tests
 * Pairwise:   9 tests covering all 37 pairs
 *
 * Username used: "admin" (reversed: "nimda")
 */
public class UsernameRulePairwiseTest {

    // ---------------------------------------------------------------
    // PW-1: matchBackwards=true, ignoreCase=true, StartsWith, forward_match
    // Password "adminSecure123" starts with "admin" -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testPW1() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "adminSecure123");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "PW-1: password starts with username -> should fail");
    }

    // ---------------------------------------------------------------
    // PW-2: matchBackwards=true, ignoreCase=false, EndsWith, reversed_match
    // Password "Securenimda" ends with "nimda" (reversed "admin") -> FAIL REVERSED
    // ---------------------------------------------------------------
    @Test
    void testPW2() {
        UsernameRule rule = new UsernameRule(true, false, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "Securenimda");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "PW-2: password ends with reversed username -> should fail");
    }

    // ---------------------------------------------------------------
    // PW-3: matchBackwards=false, ignoreCase=true, EndsWith, no_match
    // Password "MyStr0ngPwd!" does not contain "admin" at all -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testPW3() {
        UsernameRule rule = new UsernameRule(false, true, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "MyStr0ngPwd!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "PW-3: password does not contain username -> should pass");
    }

    // ---------------------------------------------------------------
    // PW-4: matchBackwards=false, ignoreCase=false, Contains, forward_match
    // Password "theadminpwd" contains "admin" (case-sensitive) -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testPW4() {
        UsernameRule rule = new UsernameRule(false, false, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "theadminpwd");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "PW-4: password contains username -> should fail");
    }

    // ---------------------------------------------------------------
    // PW-5: matchBackwards=true, ignoreCase=true, Contains, reversed_match
    // Password "xyznimdaQRS" contains "nimda" (reversed "admin") -> FAIL REVERSED
    // Forward check: "xyznimdaqrs" does not contain "admin" -> no forward match
    // Backward check: "xyznimdaqrs" contains "nimda" -> reversed match found
    // ---------------------------------------------------------------
    @Test
    void testPW5() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "xyznimdaQRS");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "PW-5: password contains reversed username -> should fail");
    }

    // ---------------------------------------------------------------
    // PW-6: matchBackwards=true, ignoreCase=false, StartsWith, no_match
    // Password "Str0ngP@sswd" does not start with "admin" or "nimda" -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testPW6() {
        UsernameRule rule = new UsernameRule(true, false, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "Str0ngP@sswd");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "PW-6: password does not start with username or reversed -> should pass");
    }

    // ---------------------------------------------------------------
    // PW-7: matchBackwards=false, ignoreCase=true, StartsWith, reversed_match
    // Password "nimdaSecure" starts with "nimda" (reversed username),
    // BUT matchBackwards=false so reversed is never checked.
    // Forward: "nimdasecure" does not start with "admin" -> no match -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testPW7() {
        UsernameRule rule = new UsernameRule(false, true, MatchBehavior.StartsWith);
        PasswordData pd = new PasswordData("admin", "nimdaSecure");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "PW-7: reversed username present but matchBackwards=false -> should pass");
    }

    // ---------------------------------------------------------------
    // PW-8: matchBackwards=true, ignoreCase=true, EndsWith, forward_match
    // Password "Secureadmin" ends with "admin" -> FAIL
    // ---------------------------------------------------------------
    @Test
    void testPW8() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.EndsWith);
        PasswordData pd = new PasswordData("admin", "Secureadmin");
        RuleResult result = rule.validate(pd);
        assertFalse(result.isValid(),
            "PW-8: password ends with username -> should fail");
    }

    // ---------------------------------------------------------------
    // PW-9: matchBackwards=true, ignoreCase=true, Contains, no_match
    // Password "Str0ngP@ss!" contains neither "admin" nor "nimda" -> SUCCESS
    // ---------------------------------------------------------------
    @Test
    void testPW9() {
        UsernameRule rule = new UsernameRule(true, true, MatchBehavior.Contains);
        PasswordData pd = new PasswordData("admin", "Str0ngP@ss!");
        RuleResult result = rule.validate(pd);
        assertTrue(result.isValid(),
            "PW-9: password contains neither username nor reversed -> should pass");
    }
}
