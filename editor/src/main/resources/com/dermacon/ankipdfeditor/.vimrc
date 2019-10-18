" ----------------- Anki-Editor -------------------
" * Version: 1.1
" * Author: github/dermacon
" * Repo: anki-pdf-editor

" ________ general ________
" treat wrapped lines as visual lines
noremap j gj
noremap k gk

" costum syntax highlighting
:colorscheme elflord
:match Constant /\v(*)/
:2match Keyword /\v(front:|back:|tags:)/


" _______ commands _______

" key: z or shift + z
" turn page - copy response to default register
:nmap z :let @" = system("curl -s http://localhost:8080/turnNextPage")<CR>
:nmap Z :let @" = system("curl -s http://localhost:8080/turnPrevPage")<CR>

" key: '['
" prints the current page tag to the current cursor position
let apiUrl = 'curl -s http://localhost:8080/getCurrPage'
:nmap [ "=system(apiUrl)<C-M>p

"key: ']'
" create a new card in the current file
" copies the tags from the last card
:nmap ] /---<CR>?tags<CR>jV/---<CR>y/---<CR>o<CR>front:<CR><CR>
\<CR><BS>back:<CR><CR>
\<CR><BS>tags:<Esc>p?front:<CR>o

" key: tab / shift + tab
" press n or N to repeat command
" tab between fields
:inoremap <Tab> <Esc>/:<CR>jI
:nnoremap <Tab> /:<CR>j0

:inoremap <S-Tab> <Esc>/:<CR>NNjI
:nnoremap <S-Tab> /:<CR>NNj0

