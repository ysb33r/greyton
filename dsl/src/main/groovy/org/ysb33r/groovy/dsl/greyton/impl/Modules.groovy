package org.ysb33r.groovy.dsl.greyton.impl

import com.google.common.collect.ImmutableSet
import com.google.inject.Module
import org.jclouds.ssh.jsch.config.JschSshClientModule
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule

/**
 * Created by schalkc on 12/04/2014.
 */
class Modules {
    private Boolean logging = false
    private Boolean ssh = false

    ImmutableSet<Module> modules() {
        def mods= []

        if(logging) {
            mods << new SLF4JLoggingModule()
        }
        if(ssh) {
            mods << new JschSshClientModule()
        }
        mods ?  ImmutableSet.<Module>copyOf(mods) :null
    }

    void ssh(final Boolean b) {ssh=b}
    void logging(final Boolean b) {ssh=b}

    static final Boolean on = true
    static final Boolean off = false
}
