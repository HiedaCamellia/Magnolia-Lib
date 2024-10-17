package org.hiedacamellia.magnolialib.scripting.wrapper;

import org.hiedacamellia.magnolialib.world.team.MagnoliaTeam;

public class TeamJS extends AbstractJS<MagnoliaTeam> {
    public TeamJS(MagnoliaTeam team) {
        super(team);
    }

    public TeamStatusJS status() {
        return WrapperRegistry.wrap(this);
    }

    public int size() {
        return penguinScriptingObject.members().size();
    }
}
