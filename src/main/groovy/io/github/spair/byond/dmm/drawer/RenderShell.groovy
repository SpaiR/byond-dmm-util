package io.github.spair.byond.dmm.drawer

import io.github.spair.byond.dmm.Dmm
import io.github.spair.byond.dmm.TileItem

import static io.github.spair.byond.dmi.SpriteDir.*

class RenderShell {

    private final GroovyShell shell
    private final Binding sharedData = new Binding()

    private final varScripts = [:]

    RenderShell(Dmm dmm, List<File> scripts) {
        bindExtensionProcs()
        sharedData.dmm = dmm
        shell = new GroovyShell(getClass().getClassLoader(), sharedData)

        scripts.each { scriptFile ->
            def cleanFileName = scriptFile.name.take(scriptFile.name.lastIndexOf('.'))
            def splittedName = cleanFileName.split('~')

            def scriptType = splittedName[0]
            def objectType = splittedName[1].replace('.', '/')

            if (scriptType == 'var') {
                varScripts[objectType] = shell.parse(scriptFile)
            }
        }
    }

    void executeVarScriptIfAble(TileItem item) {
        shell.src = item
        for (String key in varScripts.keySet()) {
            if (item.isType(key)) {
                varScripts[key].run()
            }
        }
    }

    private void bindExtensionProcs() {
        List.metaClass.<TileItem>typeOnlyEach { String type, Closure closure ->
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
                return NORTH

            if (x1 == x2) {
                if (y1 < y2) {
                    NORTH
                } else {
                    SOUTH
                }
            } else if (y1 == y2) {
                if (x1 < x2) {
                    EAST
                } else {
                    WEST
                }
            } else if (x1 < x2 && y1 < y2) {
                NORTHEAST
            } else if (x1 < x2 && y1 > y2) {
                SOUTHEAST
            } else if (x1 > x2 && y1 > y2) {
                SOUTHWEST
            } else {
                NORTHWEST
            }
        }
    }
}
