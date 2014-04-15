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
