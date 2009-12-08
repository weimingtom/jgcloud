package com.jgcloud.sandbox.jme;

import com.jmex.awt.swingui.JMEDesktop;
import com.jmex.game.state.BasicGameState;
import jmetest.game.state.TestJMEDesktopState;

/**
 * The GameState that will show the username/password, server etc and have a
 * "Login" button to do all of the connection work.
 *
 * @author Richard Hawkes
 */
public class LoginGameState extends BasicGameState {

    private JMEDesktop loginDesktop;
    private TestJMEDesktopState tjmed;

    public LoginGameState() {
        super("Login Client");
    }
}
