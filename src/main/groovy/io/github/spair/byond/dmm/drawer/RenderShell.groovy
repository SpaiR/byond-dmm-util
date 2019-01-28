package io.github.spair.byond.dmm.drawer

import io.github.spair.byond.dmm.Dmm
import io.github.spair.byond.dmm.TileItem

import java.awt.image.BufferedImage

import static io.github.spair.byond.dmi.SpriteDir.*

class RenderShell {

    private final GroovyShell shell
    private final Binding sharedData = new Binding()

    private final varScripts = [:]
    private final imgScripts = [:]

    RenderShell(Dmm dmm, List<File> scripts) {
        bindExtensionProcs()
        sharedData.dmm = dmm
        shell = new GroovyShell(getClass().getClassLoader(), sharedData)

        scripts.each { scriptFile ->
            def cleanFileName = scriptFile.name.take(scriptFile.name.lastIndexOf('.'))

            cleanFileName.split('\\.').with {
                if (it[1] == 'var') {
                    varScripts[cleanFileName] = shell.parse(scriptFile)
                } else if (it[1] == 'img') {
                    imgScripts[cleanFileName] = shell.parse(scriptFile)
                } else {
                    throw new IllegalArgumentException("script without classification ($scriptFile.name)")
                }
            }
        }
    }

    void setProc(String name, Proc proc) {
        shell.setProperty(name, proc)
    }

    void executeVarScripts(TileItem item) {
        shell.src = item
        varScripts.keySet().each { key ->
            varScripts[key].run()
        }
    }

    BufferedImage executeImgScripts(TileItem item, BufferedImage sprite) {
        shell.src = item
        shell.sprite = sprite
        def finalSprite = sprite
        imgScripts.keySet().each { key ->
            finalSprite = imgScripts[key].run()
        }
        finalSprite
    }

    private void bindExtensionProcs() {
        List.metaClass.<TileItem>eachForTypeOnly { String type, Closure closure ->
            delegate.each { TileItem item ->
                if (item.isType(type)) {
                    closure(item)
                }
            }
        }

        sharedData.oRange = { int range ->
            Dmm dmm = sharedData.dmm
            TileItem item = sharedData.src

            def list = [] as List

            list.addAll(dmm.getTile(item.x + range, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x + range, item.y - range).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y - range).tileItems)

            list.addAll(dmm.getTile(item.x + range, item.y).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y).tileItems)
            list.addAll(dmm.getTile(item.x, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x, item.y - range).tileItems)

            list
        }

        sharedData.getDir = { TileItem loc1, TileItem loc2 ->
            int x1 = loc1.x, x2 = loc2.x, y1 = loc1.y, y2 = loc2.y

            if (loc1.x == loc2.x && loc1.y == loc2.y)
                return 0

            def dir
            if (x1 == x2) {
                if (y1 < y2) {
                    dir = NORTH
                } else {
                    dir = SOUTH
                }
            } else if (y1 == y2) {
                if (x1 < x2) {
                    dir = EAST
                } else {
                    dir = WEST
                }
            } else if (x1 < x2 && y1 < y2) {
                dir = NORTHEAST
            } else if (x1 < x2 && y1 > y2) {
                dir = SOUTHEAST
            } else if (x1 > x2 && y1 > y2) {
                dir = SOUTHWEST
            } else {
                dir = NORTHWEST
            }
            dir.dirValue
        }
    }
}
