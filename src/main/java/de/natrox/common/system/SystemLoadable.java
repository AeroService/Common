package de.natrox.common.system;

import de.natrox.common.Loadable;

@Deprecated
public interface SystemLoadable extends Loadable {

    void shutdown();

}