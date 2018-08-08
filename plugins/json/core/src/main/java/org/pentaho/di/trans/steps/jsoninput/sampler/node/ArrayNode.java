/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2018 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.jsoninput.sampler.node;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that holds children of Node type
 *
 * Created by bmorrise on 8/7/18.
 */
public class ArrayNode implements Node {

  private List<Node> children = new ArrayList<>();

  public void addChild( Node child ) {
    children.add( child );
  }

  public List<Node> getChildren() {
    return children;
  }

  /**
   * Combines two array nodes
   *
   * @param arrayNode - The ArrayNode to combine with this one
   */
  public void combine( ArrayNode arrayNode ) {
    children.addAll( arrayNode.getChildren() );
  }

  /**
   * Remove duplicate child nodes
   */
  public void dedupe() {
    ObjectNode objectNode = new ObjectNode();
    ArrayNode arrayNode = new ArrayNode();
    for ( int i = 0; i < children.size(); i++ ) {
      Node child = children.get( i );
      if ( child instanceof ObjectNode ) {
        objectNode.combine( (ObjectNode) child );
      }
      if ( child instanceof ArrayNode ) {
        arrayNode.combine( (ArrayNode) child );
      }
    }
    children.clear();
    if ( objectNode.getValues().size() > 0 ) {
      children.add( objectNode );
    }
    if ( arrayNode.getChildren().size() > 0 ) {
      children.add( arrayNode );
    }
  }

  @Override
  public String output() {
    String output = "[\n";
    for ( Node child : children ) {
      output += child.output();
    }
    return output + "]";
  }
}
