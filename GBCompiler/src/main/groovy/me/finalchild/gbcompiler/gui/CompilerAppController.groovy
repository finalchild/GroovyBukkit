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

package me.finalchild.gbcompiler.gui

import com.google.common.io.Files
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import me.finalchild.gbcompiler.GBCompiler

import java.nio.file.Path
import java.nio.file.Paths

class CompilerAppController {

    @FXML
    private Button compileButton
    @FXML
    private TextField fileName
    @FXML
    private Button browseScriptButton
    @FXML
    private TextField target
    @FXML
    private Button browseTargetButton

    @FXML
    private void compile(ActionEvent event) {
        Path file = Paths.get(fileName.text)
        Path target = Paths.get(target.text)
        new GBCompiler(
                file: file,
                targetFile: target,
                id: Files.getNameWithoutExtension(file.toString()),
                name: Files.getNameWithoutExtension(file.toString()),
                version: '1.0.0-SNAPSHOT'
        ).run()
    }

    @FXML
    private void browseScript(ActionEvent event) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.title = 'Open Script File'
        fileChooser.extensionFilters.addAll(
                new FileChooser.ExtensionFilter('Groovy Scripts', '*.groovy', '*.gvy', '*.gy', '*.gsh')
        )
        fileChooser.initialDirectory = new File(System.getProperty("user.dir"))
        File file = fileChooser.showOpenDialog(browseScriptButton.scene.window)
        if (file != null) {
            fileName.text = file.toString()
            target.text = new File(file.parentFile, Files.getNameWithoutExtension(file.toString()) + '.jar').toString()
        }
    }

    @FXML
    private void browseTarget(ActionEvent event) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.title = 'Choose where to save the plugin file'
        fileChooser.extensionFilters.addAll(
                new FileChooser.ExtensionFilter('Java™ Archive', '*.jar')
        )
        fileChooser.initialDirectory = new File(System.getProperty("user.dir"))
        File file = fileChooser.showSaveDialog(browseTargetButton.scene.window)
        if (file != null) {
            target.text = file.toString()
        }
    }

}
