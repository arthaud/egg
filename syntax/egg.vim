" Vim syntax file
" Language:		Egg
" URL:			https://github.com/arthaud/egg
" Maintainer:		Martin Carton <cartonmartin+egg@gmail.com>
" ----------------------------------------------------------------------------

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
syn match	eggRule		"\<\%(\u\|_\)\%(\u\|_\)*\>"

" String
syn match	eggStringEscape "\\."	contained display
syn match	eggQuoteEscape  "\\'"	contained display
syn region	eggString matchgroup=eggStringDelimiter start="\""	end="\""	skip="\\\\\|\\\"" contains=eggStringEscape,@Spell keepend
syn region	eggString matchgroup=eggStringDelimiter start="'"	end="'"		skip="\\\\\|\\'"  contains=eggQuoteEscape,@Spell
syn region	eggString matchgroup=eggStringDelimiter start="\$"	end="\$"	skip="\\\\\|\\\$" contains=eggStringSpecial

" Comments
syn keyword	eggTodo		FIXME NOTE TODO XXX containedin=eggComment contained
syn match	eggComment	"--.*" contains=eggTodo,@Spell
syn region	eggComment	start="-\*" end="\*-" contains=eggTodo,@Spell fold extend

" Blocs
syn region	eggBraces	start="{"	end="}"			contains=@eggKeyword,eggComment,eggRule,eggString
syn region	eggDoBloc	start="\<do\>"	end="\<end\>"		contains=@eggKeyword,eggComment,eggRule,eggString
" TODO: find a way to exclude comments before the next group
syn region	eggRuleBloc	start="^.*->"	end="\%(^.*->\)\@="	contains=eggComment,eggBraces,eggFn,eggRule,eggString fold

" Keywords
syn match	eggControl		"\<\%(do\|if\|then\|else\|elseif\|exception\|with\|match\|end\)\>[?!]\@!"
syn match	eggControl		"\<\%(new\|global\|local\|sugar\|space\|term\|is\|inh\|syn\|for\|option\|aka\|comment\|macro\|compil\)\>[?!]\@!"
syn match	eggBoolean		"\<\%(true\|false\)\>[?!]\@!"
syn match	eggPseudoVariable	"\<\%(nil\|null\)\>[?!]\@!"
syn cluster	eggKeyword		contains=eggControl,eggBoolean,eggPseudoVariable

" Set types
if !exists("egg_no_identifiers")
  hi def link eggIdentifier		Identifier
else
  hi def link eggIdentifier		NONE
endif

hi def link eggBoolean			Boolean
hi def link eggComment			Comment
hi def link eggControl			Keyword
hi def link eggFn			eggIdentifier
hi def link eggMultilineComment		Comment
hi def link eggNumber			Number
hi def link eggOperator			Operator
hi def link eggPseudoVariable		Constant
hi def link eggPseudoVariable		Constant
hi def link eggRule			Type
hi def link eggString			String
hi def link eggStringDelimiter		String
hi def link eggStringEscape		Special
hi def link eggTodo			Todo

let b:current_syntax = "egg"

" vim: nowrap sw=2 sts=2 ts=8 noet:
