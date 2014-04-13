package org.ysb33r.groovy.dsl.greyton.impl

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jclouds.ContextBuilder
import org.jclouds.compute.ComputeServiceContext
import org.ysb33r.groovy.dsl.greyton.Cloud
import com.google.inject.Module

/**
 * Created by schalkc on 10/04/2014.
 */
@Slf4j
class ComputeServiceFactory {

    private final ALIASES = [
            'ec2': 'aws-ec2'
    ]

    private Cloud cloud

    ComputeServiceFactory(Cloud cld) {
        cloud = cld
    }

    //@CompileStatic
    static void bind(Cloud cld,@DelegatesTo(ComputeServiceFactory) Closure c) {
        def newc=c.clone()
        newc.delegate = new ComputeServiceFactory(cld)
        newc.resolveStrategy = Closure.DELEGATE_FIRST
        newc()
    }

    def methodMissing(final String name,args) {

        if(args.size()==2) {
            String alias = args[0].toString()
            def object = args[1]

            if(ALIASES[alias]) {
                alias = ALIASES[alias]
            }

            switch(object) {
                case Closure:
                    def context = ContextBuilder.newBuilder(name)
                    ComputeServiceOptions.bind(name.toString(),context,object)
                    context.name(alias.toString())
//                   context.modules( ImmutableSet.<Module> of(new SLF4JLoggingModule()) )

                    log.info "Created farm '${alias.toString()}' of type '${name}'"
                    return cloud.farmMap[alias.toString()]= context.buildView(ComputeServiceContext.class).computeService
            }
        }

        throw new MissingMethodException(name,this.class,args)
    }

    @CompileStatic
    void delete(final String name) { cloud.delete farm : name }
}
