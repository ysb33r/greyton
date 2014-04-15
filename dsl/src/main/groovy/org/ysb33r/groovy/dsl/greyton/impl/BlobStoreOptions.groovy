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
import groovy.transform.TupleConstructor
import org.jclouds.filesystem.reference.FilesystemConstants

/**
 * Created by schalkc on 10/04/2014.
 */
@TupleConstructor
class BlobStoreOptions {

    private final static REPLACEMENTS = [
            'filesystem' : [ baseDir : FilesystemConstants.PROPERTY_BASEDIR ]
    ]

    String name
    def context

    static def bind(name,ctxt,@DelegatesTo(BlobStoreOptions) Closure c) {
        def bso=new BlobStoreOptions(name,ctxt)
        c.delegate = bso
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
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

    // modules

}
