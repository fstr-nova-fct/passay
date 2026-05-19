/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.passay.PasswordData;
import org.passay.RuleResult;
import static org.assertj.core.api.Assertions.*;

/**
 * Black-box tests for {@link LengthRule#validate(org.passay.PasswordData)}.
 *
 * <p>Implements test batteries from equivalence class partitioning (weak) and
 * simple boundary value analysis (section 3.1.1). Configuration: minimum length
 * 8, maximum length 16 (inclusive).</p>
 */
public class LengthRuleBlackBoxTest
{

  private static final int MIN_LENGTH = 8;

  private static final int MAX_LENGTH = 16;

  private LengthRule rule;


  @BeforeEach
  public void setUp()
  {
    rule = new LengthRule(MIN_LENGTH, MAX_LENGTH);
  }


  // --- Weak Equivalence Class (WEC) ---

  /** WEC1 — EC1: password too short (length &lt; 8). */
  @Test
  public void WEC1_passwordTooShort()
  {
    assertFailure("abc", LengthRule.ERROR_CODE_MIN);
  }


  /** WEC2 — EC2: password in valid range (8 &le; length &le; 16). */
  @Test
  public void WEC2_passwordValid()
  {
    assertSuccess("validpass");
  }


  /** WEC3 — EC3: password too long (length &gt; 16). */
  @Test
  public void WEC3_passwordTooLong()
  {
    assertFailure("thispasswordiswaytoolong", LengthRule.ERROR_CODE_MAX);
  }


  // --- Simple Boundary Value Analysis (SBVA) ---

  /** SBVA1 — length = min − 1 (7). */
  @Test
  public void SBVA1_lengthBelowMinimum()
  {
    assertFailure(passwordOfLength(7), LengthRule.ERROR_CODE_MIN);
  }


  /** SBVA2 — length = min (8). */
  @Test
  public void SBVA2_lengthAtMinimum()
  {
    assertSuccess(passwordOfLength(8));
  }


  /** SBVA3 — length = min + 1 (9). */
  @Test
  public void SBVA3_lengthJustAboveMinimum()
  {
    assertSuccess(passwordOfLength(9));
  }


  /** SBVA4 — length = max − 1 (15). */
  @Test
  public void SBVA4_lengthJustBelowMaximum()
  {
    assertSuccess(passwordOfLength(15));
  }


  /** SBVA5 — length = max (16). */
  @Test
  public void SBVA5_lengthAtMaximum()
  {
    assertSuccess(passwordOfLength(16));
  }


  /** SBVA6 — length = max + 1 (17). */
  @Test
  public void SBVA6_lengthAboveMaximum()
  {
    assertFailure(passwordOfLength(17), LengthRule.ERROR_CODE_MAX);
  }


  private static String passwordOfLength(final int length)
  {
    final StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append('a');
    }
    return sb.toString();
  }


  private void assertSuccess(final String password)
  {
    final RuleResult result = rule.validate(new PasswordData(password));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getDetails()).isEmpty();
  }


  private void assertFailure(final String password, final String expectedErrorCode)
  {
    final RuleResult result = rule.validate(new PasswordData(password));
    assertThat(result.isValid()).isFalse();
    assertThat(result.getDetails()).hasSize(1);
    assertThat(result.getDetails().get(0).getErrorCode()).isEqualTo(expectedErrorCode);
  }

}
