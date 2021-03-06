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
