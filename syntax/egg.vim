" Vim syntax file
" Language:		Egg
" URL:			https://github.com/arthaud/egg
" Maintainer:		Martin Carton <cartonmartin+egg@gmail.com>
" ----------------------------------------------------------------------------
" To use, add that file to ~/.vim/syntax and the line
"      autocmd BufNewFile,BufRead *.egg set filetype=egg
" to your .vimrc

if exists("b:current_syntax")
  finish
endif

if has("folding") && exists("egg_fold")
  setlocal foldmethod=syntax
endif

" Operators
if exists("egg_operators")
  syn match  eggOperator "[~!&|*/%+-]\|<=\|==\|>=\|/=\|:=\|&&\|||\|->\|@"
endif

" Misc
syn match	eggFn		"#\w*"
syn match	eggNumber	"\d\%(\.\d\)*"
syn region	eggMultilineComment start="-[*]" end="[*]-" contains=eggTodo,@Spell transparent fold keepend
syn match	eggRule		"\<\%(\u\|_\)\%(\u\|_\|\d\)*\>"

" String
syn match	eggStringEscape "\\."	contained display
syn match	eggQuoteEscape  "\\'"	contained display
syn region	eggString matchgroup=eggStringDelimiter		start="\""	end="\""	skip="\\\\\|\\\"" contains=eggStringEscape,@Spell keepend
syn region	eggString matchgroup=eggStringDelimiter 	start="'"	end="'"		skip="\\\\\|\\'"  contains=eggQuoteEscape,@Spell
syn region	eggDollarString matchgroup=eggStringDelimiter	start="\$"	end="\$"	skip="\\\\\|\\\$" contains=eggStringSpecial

" Comments
syn keyword	eggTodo		FIXME NOTE TODO XXX containedin=eggComment contained
syn match	eggComment	"--.*" contains=eggTodo,@Spell
syn region	eggComment	start="-\*" end="\*-" contains=eggTodo,@Spell fold extend

" Blocs
syn region	eggBraces	start="{"	end="}"			contains=@eggKeyword,eggComment,eggRule,eggString,eggNumber,eggBraces
" TODO: find a way to exclude comments before the next group
syn region	eggRuleBloc	start="^.*->"	end="\%(^.*->\)\@="	contains=eggComment,eggBraces,eggFn,eggRule,eggString,eggGlobal fold
" end and start volontarly reversed so that ";" is not colored as a Keyword
syn region	eggAttrDecl	end=";" matchgroup=eggAttrDeclKw 		start="\<\%(inh\|syn\)\>"	contains=eggComment,eggRule,eggFor,eggAttrDeclKw fold transparent
syn region	eggSugar	matchgroup=eggAttrDeclKw start="\<\%(comment\|space\|term\|sugar\)\>" 	end="^\%(comment\|space\|term\|sugar\)\@!"	contains=eggSugarKw,eggString,eggDollarString fold transparent

" Keywords
syn match	eggControl		"\<\%(do\|if\|then\|else\|elseif\|exception\|with\|match\|do\|end\|case\|default\)\>[?!]\@!"
syn match	eggControl		"\<\%(new\|global\|local\|option\|macro\|compil\)\>[?!]\@!"
syn match	eggGlobal		"\<global\>[?!]\@!"
syn match	eggAttrDeclKw		"\<for\>[?!]\@!"
syn match	eggSugarKw		"\<\%(is\|aka\|comment\|space\|term\|sugar\)\>[?!]\@!" contained display
syn match	eggBoolean		"\<\%(true\|false\)\>[?!]\@!"
syn match	eggPseudoVariable	"\<\%(nil\|null\)\>[?!]\@!"
syn match	eggElseIf		"\<\%(else if\)\>[?!]\@!"
syn cluster	eggKeyword		contains=eggControl,eggGlobal,eggBoolean,eggPseudoVariable,eggElseIf

" Set types
if !exists("egg_no_identifiers")
  hi def link eggIdentifier		Identifier
else
  hi def link eggIdentifier		NONE
endif

hi def link eggAttrDeclKw		Keyword
hi def link eggBoolean			Boolean
hi def link eggComment			Comment
hi def link eggControl			Keyword
hi def link eggDollarString		String
hi def link eggFn			eggIdentifier
hi def link eggGlobal			Keyword
hi def link eggMultilineComment		Comment
hi def link eggNumber			Number
hi def link eggOperator			Operator
hi def link eggPseudoVariable		Constant
hi def link eggPseudoVariable		Constant
hi def link eggRule			Type
hi def link eggString			String
hi def link eggStringDelimiter		String
hi def link eggStringEscape		Special
hi def link eggSugar			Comment
hi def link eggSugarKw			Keyword
hi def link eggTodo			Todo
hi def link eggElseIf			Error

let b:current_syntax = "egg"

" vim: nowrap sw=2 sts=2 ts=8 noet:
