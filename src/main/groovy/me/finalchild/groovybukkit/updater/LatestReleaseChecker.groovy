/*
 * This file is part of GroovyBukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Final Child
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.finalchild.groovybukkit.updater

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import groovy.transform.ToString
import me.finalchild.groovybukkit.GroovyBukkit

import javax.net.ssl.HttpsURLConnection

class LatestReleaseChecker {

    private static URL url = new URL('https://api.github.com/repos/finalchild/GroovyBukkit/releases/latest')

    /**
     * Returns the ReleaseInfo if the plugin is updatable
     * @return the ReleaseInfo if the plugin is updatable
     */
    static Optional<ReleaseInfo> getLatestRelease() {
        getLatestReleaseWithURL(url)
    }

    private static Optional<ReleaseInfo> getLatestReleaseWithURL(URL url) {
        HttpsURLConnection connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = 'GET'
        connection.setRequestProperty 'Accept', 'application/vnd.github.v3+json'
        connection.setRequestProperty 'User-Agent', 'GroovyBukkit'
        connection.setRequestProperty 'Time-Zone', TimeZone.default.ID

        switch (connection.responseCode) {
            case 200:
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.inputStream))
                JsonObject o = new JsonParser().parse reader asJsonObject

                String version1 = GroovyBukkit.instance.description.version
                String version2 = o.getAsJsonPrimitive 'name' asString
                version2 = version2.substring(1, version2.length())

                if (version1 != version2) {
                    return Optional.of(new ReleaseInfo(url: o.get('html_url'), name: version2))
                } else {
                    return Optional.empty()
                }
            case 301:
                LatestReleaseChecker.url = new URL(connection.getHeaderField('Location'))
                return getLatestRelease()
            case 302:
            case 307:
                return getLatestReleaseWithURL(new URL(connection.getHeaderField('Location')))
            default:
                return Optional.empty()
        }

    }

    @ToString
    static class ReleaseInfo {
        String url
        String name
    }
}
