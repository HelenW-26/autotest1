#include <MsgBoxConstants.au3>

getSaveAsFileName()

Func getSaveAsFileName()
   $windowHandle=WinGetHandle("Save As")
   WinActivate($windowHandle)
   Sleep(100)
   Local $sText = ControlGetText($windowHandle, "Save As", "[CLASS:Edit; INSTANCE:1]")
   ConsoleWrite($sText)
   ;MsgBox($MB_SYSTEMMODAL, " ", "The text in Edit is: " & $sText)
   ControlClick($windowHandle, "Save As","[CLASS:Button; INSTANCE:3]")
   Exit ($sText)

EndFunc