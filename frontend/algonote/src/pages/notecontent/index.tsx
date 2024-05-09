import { css } from '@codemirror/lang-css'
import { html } from '@codemirror/lang-html'
import { java } from '@codemirror/lang-java'
import { javascript } from '@codemirror/lang-javascript'
import { markdown } from '@codemirror/lang-markdown'
import { python } from '@codemirror/lang-python'
import { EditorView } from '@codemirror/view'
import CodeMirror from '@uiw/react-codemirror'
import useNoteStore from '@/stores/note-store'

const NoteContent = () => {
  const { tabs, curSelectedIdx, updateTab } = useNoteStore()
  const currentTab = tabs.find((tab) => tab.idx === curSelectedIdx)
  const currentContent = currentTab?.content
  const handleContent = (value: string) => {
    if (currentTab) {
      updateTab(curSelectedIdx, { title: currentTab?.title, content: value })
    }
  }

  return (
    <CodeMirror
      value={currentContent}
      width="100%"
      height="100%"
      extensions={[
        markdown({
          defaultCodeLanguage: python(),
        }),
        java(),
        javascript(),
        css(),
        html(),
        EditorView.lineWrapping,
      ]}
      onChange={handleContent}
      theme={EditorView.theme({
        '&': {
          color: 'black',
          backgroundColor: 'white',
        },
        '.cm-content': {
          caretColor: 'black',
          fontFamily: 'Pretendard, sans-serif',
          fontSize: '1.2rem',
          lineHeight: '1.5',
        },
        '&.cm-focused': {
          outline: 'none',
        },
        '&.cm-focused .cm-cursor': { borderLeftColor: 'black' },
        '&.cm-focused .cm-selectionBackground, ::selection': {
          backgroundColor: '#d9d9d9',
        },
        '.cm-gutters': {
          display: 'none',
        },
        '.cm-line': {
          backgroundColor: 'white',
        },
        '.cm-link': {
          color: '#065fd4',
          textDecoration: 'underline',
        },
        '.cm-strong': {
          fontWeight: 'bold',
        },
        '.cm-emphasis': {
          fontStyle: 'italic',
        },
        '.cm-placeholder': {
          color: '#adb5bd',
        },
        '::-webkit-scrollbar': {
          width: '4px',
          height: '4px',
        },
        '::-webkit-scrollbar-thumb': {
          background: '#212529',
          borderRadius: '4px',
        },
      })}
    />
  )
}

export default NoteContent
