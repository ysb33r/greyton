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

package org.ysb33r.groovy.dsl.greyton.impl

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jclouds.ContextBuilder
import org.jclouds.compute.ComputeServiceContext
import org.jclouds.compute.domain.TemplateBuilder
import org.ysb33r.groovy.dsl.greyton.Cloud
import org.ysb33r.groovy.dsl.greyton.SyntaxException

/**
 * Created by schalkc on 13/04/2014.
 */
@Slf4j
class TemplateFactory {

    private Cloud cloud

    TemplateFactory(Cloud cld) {
        cloud = cld
    }

    static void bind(Cloud cld,@DelegatesTo(TemplateFactory) Closure c) {
        def newc=c.clone()
        newc.delegate = new TemplateFactory(cld)
        newc.resolveStrategy = Closure.DELEGATE_FIRST
        newc()
    }

    def methodMissing(final String name,args) {

        if(args.size()==2) {
            if(!cloud.farms[name]) {
                throw new SyntaxException("'${name}' is not a valid farm and cannot be used as a keyword inside a templates {} block")
            }
            String alias = args[0].toString()
            def configObject = args[1]

            switch(configObject) {
                case Closure:
                    TemplateBuilder tb = this.cloud.farms[name].templateBuilder()
                    TemplateFactoryOptions.bind(tb,configObject)
                    log.info "Created template '${alias.toString()}' linked to farm '${name}'"
                    return cloud.templateMap[alias.toString()]= tb.build()
            }
        }

        throw new MissingMethodException(name,this.class,args)
    }

    @CompileStatic
    void delete(final String name) { cloud.delete farm : name }
}
