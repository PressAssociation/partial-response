/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Press Association Limited
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.pressassociation.pr.ast.visitor;

import com.google.common.collect.Lists;

import com.pressassociation.pr.ast.*;

import java.util.Deque;

import static com.google.common.base.Preconditions.checkState;

/**
 * Visitor that copies (deeply) the nodes of an ast.
 *
 * @author Matt Nathan
 */
public class CopyVisitor extends TransformingVisitor<AstNode> {

  private final Deque<AstNode> stack = Lists.newLinkedList();

  @Override
  public void visitFields(Fields fields) {
    super.visitFields(fields);
    AstNode next = stack.removeLast();
    Field field = (Field) stack.removeLast();
    stack.addLast(createFieldsCopy(field, next));
  }

  @Override
  public void visitPath(Path path) {
    super.visitPath(path);
    Field suffix = (Field) stack.removeLast();
    Node prefix = (Node) stack.removeLast();
    stack.addLast(createPathCopy(prefix, suffix));
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    super.visitSubSelection(subSelection);
    AstNode fields = stack.removeLast();
    Name name = (Name) stack.removeLast();
    stack.addLast(createSubSelectionCopy(name, fields));
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    stack.addLast(wildcard);
  }

  @Override
  public void visitWord(Word word) {
    stack.addLast(createWordCopy(word.getStringValue()));
  }

  @Override
  public AstNode getResult() {
    checkState(!stack.isEmpty(), "Cannot get the result when the visitor hasn't been used yet");
    return stack.getLast();
  }

  protected Fields createFieldsCopy(Field field, AstNode next) {
    return new Fields(field, next);
  }

  protected Path createPathCopy(Node prefix, Field suffix) {
    return new Path(prefix, suffix);
  }

  protected SubSelection createSubSelectionCopy(Name name, AstNode fields) {
    return new SubSelection(name, fields);
  }

  protected Word createWordCopy(String stringValue) {
    return new Word(stringValue);
  }
}
