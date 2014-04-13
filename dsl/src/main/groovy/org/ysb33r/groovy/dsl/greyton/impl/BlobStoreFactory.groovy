package org.ysb33r.groovy.dsl.greyton.impl

import com.google.common.collect.ImmutableSet
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.jclouds.ContextBuilder
import org.jclouds.blobstore.BlobStoreContext
import org.ysb33r.groovy.dsl.greyton.Cloud


/**
 * Created by schalkc on 10/04/2014.
 */
@Slf4j
class BlobStoreFactory {

    private final static ALIASES = [
        's3' : 'aws-s3'
    ]

    private Cloud cloud

    BlobStoreFactory(Cloud cld) {cloud=cld}

    static void bind(Cloud cld,@DelegatesTo(BlobStoreFactory) Closure c) {
        def newc=c.clone()
        newc.delegate = new BlobStoreFactory(cld)
        newc.resolveStrategy = Closure.DELEGATE_FIRST
        newc()
    }

    def methodMissing(String name,args) {

        if(args.size()==2) {
            String alias = args[0].toString()
            def object = args[1]

            if(ALIASES[alias]) {
                alias = ALIASES[alias]
            }

            switch(object) {
                case Closure:
                    def context = ContextBuilder.newBuilder(name)
                    BlobStoreOptions.bind(name.toString(),context,object)
                    context.name(alias.toString())
//                   context.modules( ImmutableSet.<Module> of(new SLF4JLoggingModule()) )
                    log.info "Created blobstore '${alias.toString()}' of type '${name}'"
                    return cloud.storageMap[alias.toString()]= context.buildView(BlobStoreContext.class).blobStore
            }
        }

        throw new MissingMethodException(name,this.class,args)
    }

    @CompileStatic
    void delete(final String name) { cloud.delete store : name }

}

//Properties p = new Properties()
////        String prefix = properties.type.toString().capitalize() + 'Constants'
////        properties.each { String k, Object v ->
////            if( k != 'type') {
////                String propName = "PROPERTY_${k.toUpperCase()}"
////
////                p.setProperty("${prefix}"."${propName}",v.toString())
////                if ("${prefix}".metaClass.hasProperty()
////            }
////        }
////        Closure newc = c.clone()
////        newc.delegate = ?
//
//def context = ContextBuilder.newBuilder(properties.type)
//
//context.overrides(p)
//
//// Execute closure in here
//
//return context.buildView(BlobStoreContext.class)//.blobStore
