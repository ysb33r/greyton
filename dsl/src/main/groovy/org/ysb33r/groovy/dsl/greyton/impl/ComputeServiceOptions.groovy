package org.ysb33r.groovy.dsl.greyton.impl

import com.google.common.collect.ImmutableSet
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.jclouds.filesystem.reference.FilesystemConstants
import com.google.inject.Module


/**
 * Created by schalkc on 10/04/2014.
 */
@TupleConstructor
@Mixin(Modules)
class ComputeServiceOptions {

    private final static REPLACEMENTS = [
            'filesystem' : [ baseDir : FilesystemConstants.PROPERTY_BASEDIR ]
    ]

    String name
    def context

    static def bind(name,ctxt,@DelegatesTo(ComputeServiceOptions) Closure c) {
        def bso=new ComputeServiceOptions(name,ctxt)
        c.delegate = bso
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
        ImmutableSet<Module> mods = bso.modules()
        if(mods ) {
            bso.context.modules( mods )
        }
    }

    def overrides(final Properties p) {
        context.overrides(p)
    }

    def overrides(final def properties) {
        Properties p = new Properties()
        def replacements = REPLACEMENTS[name] ?: [:]

        properties.each { String k, Object v ->
            if(replacements[k]) {
                p.setProperty(replacements[k],v.toString())
            } else {
                p.setProperty(k,v.toString())
            }
        }

        this.overrides(p)
    }

    def credentials(final String id,final String auth) {
        context.credentials(id,auth)
    }

    // TODO: credentials with auth object
    // def credentials( ... )

    def endpoint( final String ep) {
        context.endpoint(ep)
    }


}
