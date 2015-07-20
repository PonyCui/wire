/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.wire.internal.protoparser;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.wire.schema.Location;

import static com.squareup.wire.internal.Util.appendDocumentation;
import static com.squareup.wire.internal.Util.appendIndented;

@AutoValue
public abstract class ServiceElement {
  public static Builder builder(Location location) {
    return new AutoValue_ServiceElement.Builder()
        .location(location)
        .documentation("")
        .rpcs(ImmutableList.<RpcElement>of())
        .options(ImmutableList.<OptionElement>of());
  }

  public abstract Location location();
  public abstract String name();
  public abstract String documentation();
  public abstract ImmutableList<RpcElement> rpcs();
  public abstract ImmutableList<OptionElement> options();

  ServiceElement() {
  }

  public final String toSchema() {
    StringBuilder builder = new StringBuilder();
    appendDocumentation(builder, documentation());
    builder.append("service ")
        .append(name())
        .append(" {");
    if (!options().isEmpty()) {
      builder.append('\n');
      for (OptionElement option : options()) {
        appendIndented(builder, option.toSchemaDeclaration());
      }
    }
    if (!rpcs().isEmpty()) {
      builder.append('\n');
      for (RpcElement rpc : rpcs()) {
        appendIndented(builder, rpc.toSchema());
      }
    }
    return builder.append("}\n").toString();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder location(Location location);
    public abstract Builder name(String name);
    public abstract Builder documentation(String documentation);
    public abstract Builder rpcs(ImmutableList<RpcElement> rpcs);
    public abstract Builder options(ImmutableList<OptionElement> options);
    public abstract ServiceElement build();
  }
}
