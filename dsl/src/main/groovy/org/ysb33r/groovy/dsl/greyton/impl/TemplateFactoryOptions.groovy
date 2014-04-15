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
import org.jclouds.compute.domain.OsFamily
import org.jclouds.compute.domain.OsFamily as OSFAMILY
import org.jclouds.compute.domain.TemplateBuilder

/**
 * Created by schalkc on 13/04/2014.
 */
class TemplateFactoryOptions {

    TemplateFactoryOptions() {}
    TemplateFactoryOptions(TemplateBuilder tb) {builder=tb}

    static TemplateBuilder bind(TemplateBuilder tb,Closure c) {
        def newc=c.clone()
        TemplateFactoryOptions tfo = new TemplateFactoryOptions(tb)
        newc.delegate = tfo
        newc.resolveStrategy = Closure.DELEGATE_FIRST
        newc()
        tb
    }

    @CompileStatic
    TemplateBuilder os(final String osName) {osFamily osName}

    TemplateBuilder osFamily(final String osName) {
        builder.OsFamily(this."osName.toUpperCase()")
    }

    @CompileStatic
    TemplateBuilder os(final OSFAMILY osName) {osFamily osName}

    @CompileStatic
    TemplateBuilder osFamily(final OSFAMILY osName) {
        builder.osFamily(osName)
    }

    @CompileStatic
    TemplateBuilder profile(Closure... c) {
        c.each { Closure it -> it() }
        builder
    }

    TemplateBuilder profile(final String s) { this."${s}"() }

    final Closure smallest = { -> builder.smallest() }
    final Closure fastest =  { -> builder.fastest() }
    final Closure biggest =  { -> builder.biggest() }
    final Closure os64Bit =  { -> builder.os64Bit(true) }
    final Closure os32Bit =  { -> builder.os64Bit(false) }

    def propertyMissing(final String name) {

        final String name2=name.toUpperCase()

        try {
            return OSFAMILY."${name2}"
        } catch(MissingPropertyException)
        {}

        throw new MissingPropertyException(name,this.class)
    }

    def methodMissing(final String name,args) {

        if(args.size() == 1) {
            if(TemplateBuilder.metaClass.respondsTo(builder,name,args[0])) {
                return builder."${name}"(args[0])
            }
        }

        throw new MissingMethodException(name,this.class,args)
    }

    private TemplateBuilder builder

}
