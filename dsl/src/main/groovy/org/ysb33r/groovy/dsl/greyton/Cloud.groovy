package org.ysb33r.groovy.dsl.greyton

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jclouds.blobstore.BlobStore
import org.jclouds.compute.ComputeService
import org.ysb33r.groovy.dsl.greyton.impl.BlobStoreFactory
import org.ysb33r.groovy.dsl.greyton.impl.ComputeServiceFactory

/**
 * Created by schalkc on 10/04/2014.
 */
@Slf4j
class Cloud {

    static final Cloud cloud = new Cloud()

    private Map<String,BlobStore> storageMap = [:]
    private Map<String,ComputeService> farmMap = [:]

    Map<String,BlobStore> getStores() {storageMap}
    Map<String,ComputeService> getFarms() {farmMap}

    def call(@DelegatesTo(Cloud) Closure c) {
        def newc=c.clone()
        newc.delegate=this
        newc.call()
    }

//    def methodMissing(String name,args) {
//        // Try to find a blob
//    }

    @CompileStatic
    Map<String,BlobStore> stores(@DelegatesTo(BlobStoreFactory) Closure c) {
        BlobStoreFactory.bind(this,c)
        return storageMap
    }

    @CompileStatic
    Map<String,ComputeService> farms(@DelegatesTo(ComputeServiceFactory) Closure c) {
        ComputeServiceFactory.bind(this,c)
        return farmMap

    }

    /**
     *
     * @param item
     *
     * <code>
     *   delete farm : 'foo'
     *   delete store : 'bar'
     * </code>
     */
    @CompileStatic
    void delete( final Map<String,String> item) {
        assert item.size() != 0
        item.each { String keyword,String name ->
            switch(keyword) {
                case 'farm' :
                    if(farmMap[name]) {
                        farmMap.name.context.close()
                        farmMap.remove(name)
                    } else {
                        debug "Attempt to delete non-existing server farm '${name}'"
                    }
                    break
                case 'store' :
                    if(storageMap[name]) {
                        storageMap.name.context.close()
                        storageMap.remove(name)
                    } else {
                        debug "Attempt to delete non-existing storage '${name}'"
                    }
                    break
                default:
                    throw new SyntaxException("Invalid keyword '${keyword}' used in delete")
            }
        }
    }

    private void debug(final String msg) {
        log.debug(msg)
    }
}
