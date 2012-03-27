package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Splitter;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.MatchesPathVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matcher based of a PartialResponse Ast
 *
 * @author Matt Nathan
 */
class AstMatcher extends Matcher {
  final AstNode fields;

  AstMatcher(AstNode fields) {
    this.fields = checkNotNull(fields);
  }

  @Override
  public boolean matches(Leaf input) {
    return new MatchesPathVisitor(input.getPath()).applyTo(fields);
  }

  @Override
  protected String patternString() {
    return fields.toString();
  }
}