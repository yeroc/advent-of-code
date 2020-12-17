package org.geekden.advent.framework;

import picocli.CommandLine.IVersionProvider;

class Version implements IVersionProvider {

  @Override
  public String[] getVersion() throws Exception {
    return new String[] { Version.class.getPackage().getImplementationVersion() };
  }

}
