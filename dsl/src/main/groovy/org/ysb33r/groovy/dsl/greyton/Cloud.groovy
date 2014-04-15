// ============================================================================
// (C) Copyright Schalk W. Cronje 2014
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.groovy.dsl.greyton

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jclouds.blobstore.BlobStore
import org.jclouds.compute.ComputeService
import org.jclouds.compute.domain.Template
import org.ysb33r.groovy.dsl.greyton.impl.BlobStoreFactory
import org.ysb33r.groovy.dsl.greyton.impl.ComputeServiceFactory
import org.ysb33r.groovy.dsl.greyton.impl.TemplateFactory

/**
 * Created by schalkc on 10/04/2014.
 */
@Slf4j
class Cloud {

    static final Cloud cloud = new Cloud()

    private Map<String,BlobStore> storageMap = [:]
    private Map<String,ComputeService> farmMap = [:]
    private Map<String,Template> templateMap = [:]

    Map<String,BlobStore> getStores() {storageMap}
    Map<String,ComputeService> getFarms() {farmMap}
    Map<String,Template> getTemplates() {templateMap}

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

    @CompileStatic
    Map<String,Template> templates(@DelegatesTo(TemplateFactory) Closure c) {
        TemplateFactory.bind(this,c)
        return templateMap
    }
    /**
     *
     * @param item
     *
     * <code>
     *   delete farm : 'foo'
     *   delete store : 'bar'
     *   delete template : 'foobar'
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
                case 'template' :
                    if(templateMap[name]) {
                        templateMap.remove(name)
                    } else {
                        debug "Attempt to delete non-existing template '${name}'"
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
