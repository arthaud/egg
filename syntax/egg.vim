" Vim syntax file
" Language:		Egg
" URL:			https://github.com/arthaud/egg
" Release Coordinator:	Martin Carton <cartonmartin+egg@gmail.com>
" ----------------------------------------------------------------------------
" Maintainer:		Doug Kearns <dougkearns@gmail.com>
" Previous Maintainer:	Mirko Nasato
" Thanks to perl.vim authors, and to Reimer Behrends. :-) (MN)
" ----------------------------------------------------------------------------

" TODO: modified from ruby syntax file, probably not as complete as it should
" and there should have some ruby stuffs to be removed.
"
if exists("b:current_syntax")
  finish
endif

if has("folding") && exists("egg_fold")
  setlocal foldmethod=syntax
endif

syn cluster eggNotTop contains=@eggExtendedStringSpecial,@eggRegexpSpecial,@eggDeclaration,eggConditional,eggExceptional,eggMethodExceptional,eggTodo

if exists("egg_space_errors")
  if !exists("egg_no_trail_space_error")
    syn match eggSpaceError display excludenl "\s\+$"
  endif
  if !exists("egg_no_tab_space_error")
    syn match eggSpaceError display " \+\t"me=e-1
  endif
endif

" Operators
if exists("egg_operators")
  syn match  eggOperator "[~!&|*/%+-]\|<=\|==\|>=\|/=\|:=\|&&\|||\|->\|@"
endif

" Expression Substitution and Backslash Notation
syn match eggStringEscape "\\."	contained display
syn match eggQuoteEscape  "\\'"	contained display

syn match  eggFn		"#\w*"
syn cluster eggStringSpecial	     contains=eggInterpolation,eggNoInterpolation,eggStringEscape
syn cluster eggExtendedStringSpecial contains=@eggStringSpecial,eggNestedParentheses,eggNestedCurlyBraces,eggNestedAngleBrackets,eggNestedSquareBrackets
syn cluster eggRegexpSpecial	     contains=eggInterpolation,eggNoInterpolation,eggStringEscape,eggRegexpSpecial,eggRegexpEscape,eggRegexpBrackets,eggRegexpCharClass,eggRegexpDot,eggRegexpQuantifier,eggRegexpAnchor,eggRegexpParens,eggRegexpComment

" Numbers and ASCII Codes
syn match eggASCIICode	"\%(\w\|[]})\"'/]\)\@<!\%(?\%(\\M-\\C-\|\\C-\\M-\|\\M-\\c\|\\c\\M-\|\\c\|\\C-\|\\M-\)\=\%(\\\o\{1,3}\|\\x\x\{1,2}\|\\\=\S\)\)"
syn match eggInteger	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<0[xX]\x\+\%(_\x\+\)*\>"								display
syn match eggInteger	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<\%(0[dD]\)\=\%(0\|[1-9]\d*\%(_\d\+\)*\)\>"						display
syn match eggInteger	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<0[oO]\=\o\+\%(_\o\+\)*\>"								display
syn match eggInteger	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<0[bB][01]\+\%(_[01]\+\)*\>"								display
syn match eggFloat	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<\%(0\|[1-9]\d*\%(_\d\+\)*\)\.\d\+\%(_\d\+\)*\>"					display
syn match eggFloat	"\%(\%(\w\|[]})\"']\s*\)\@<!-\)\=\<\%(0\|[1-9]\d*\%(_\d\+\)*\)\%(\.\d\+\%(_\d\+\)*\)\=\%([eE][-+]\=\d\+\%(_\d\+\)*\)\>"	display

" Identifiers
syn match  eggConstant		"\%(\%([.@$]\@<!\.\)\@<!\<\|::\)\_s*\zs\u\w*\%(\>\|::\)\@=\%(\s*(\)\@!"

syn match  eggBlockParameter	  "\%(\h\|[^\x00-\x7F]\)\%(\w\|[^\x00-\x7F]\)*" contained
syn region eggBlockParameterList start="\%(\%(\<do\>\|{\)\s*\)\@<=|" end="|" oneline display contains=eggBlockParameter

" Normal String and Shell Command Output
syn region eggString matchgroup=eggStringDelimiter start="\""	end="\""	skip="\\\\\|\\\"" contains=@eggStringSpecial,@Spell fold
syn region eggString matchgroup=eggStringDelimiter start="'"	end="'"		skip="\\\\\|\\'"  contains=eggQuoteEscape,@Spell    fold
syn region eggString matchgroup=eggStringDelimiter start="\$"	end="\$"	skip="\\\\\|\\\$" contains=@eggStringSpecial           fold

" Generalized Single Quoted String, Symbol and Array of Strings
syn region eggSymbol matchgroup=eggSymbolDelimiter start="%s{"				   end="}"   skip="\\\\\|\\}"	fold contains=eggNestedCurlyBraces,eggDelimEscape
syn region eggSymbol matchgroup=eggSymbolDelimiter start="%s\["				   end="\]"  skip="\\\\\|\\\]"	fold contains=eggNestedSquareBrackets,eggDelimEscape
syn region eggSymbol matchgroup=eggSymbolDelimiter start="%s("				   end=")"   skip="\\\\\|\\)"	fold contains=eggNestedParentheses,eggDelimEscape

if exists('main_syntax') && main_syntax == 'eegg'
  let b:egg_no_expensive = 1
end

syn match  eggAliasDeclaration    "[^[:space:];#.()]\+" contained contains=eggSymbol,eggGlobalVariable,eggPredefinedVariable nextgroup=eggAliasDeclaration2 skipwhite
syn match  eggAliasDeclaration2   "[^[:space:];#.()]\+" contained contains=eggSymbol,eggGlobalVariable,eggPredefinedVariable
syn match  eggMethodDeclaration   "[^[:space:];#(]\+"	 contained contains=eggConstant,eggBoolean,eggPseudoVariable,eggInstanceVariable,eggClassVariable,eggGlobalVariable
syn match  eggClassDeclaration    "[^[:space:];#<]\+"	 contained contains=eggConstant,eggOperator
syn match  eggModuleDeclaration   "[^[:space:];#<]\+"	 contained contains=eggConstant,eggOperator
syn match  eggFunction "\<[_[:alpha:]][_[:alnum:]]*[?!=]\=[[:alnum:]_.:?!=]\@!" contained containedin=eggMethodDeclaration
syn match  eggFunction "\%(\s\|^\)\@<=[_[:alpha:]][_[:alnum:]]*[?!=]\=\%(\s\|$\)\@=" contained containedin=eggAliasDeclaration,eggAliasDeclaration2
syn match  eggFunction "\%([[:space:].]\|^\)\@<=\%(\[\]=\=\|\*\*\|[+-]@\=\|[*/%|&^~]\|<<\|>>\|[<>]=\=\|<=>\|===\|[=!]=\|[=!]\~\|!\|`\)\%([[:space:];#(]\|$\)\@=" contained containedin=eggAliasDeclaration,eggAliasDeclaration2,eggMethodDeclaration

syn cluster eggDeclaration contains=eggAliasDeclaration,eggAliasDeclaration2,eggMethodDeclaration,eggModuleDeclaration,eggClassDeclaration,eggFunction,eggBlockParameter

" Keywords
" Note: the following keywords have already been defined:
" begin case class def do end for if module unless until while
syn match   eggControl	       "\<\%(new\|global\|local\|sugar\|space\|term\|is\|inh\|syn\|option\|aka\|comment\|macro\|compil\|exception\)\>[?!]\@!"
syn match   eggControl	       "\<\%(and\|break\|in\|next\|not\|or\|redo\|rescue\|retry\|return\)\>[?!]\@!"
syn match   eggBoolean	       "\<\%(true\|false\)\>[?!]\@!"
syn match   eggPseudoVariable  "\<\%(nil\|null\)\>[?!]\@!"

" Expensive Mode - match 'end' with the appropriate opening keyword for syntax
" based folding and special highlighting of module/class/method definitions
if !exists("b:egg_no_expensive") && !exists("egg_no_expensive")
  " modifiers
  syn match eggConditionalModifier "\<\%(if\)\>"    display
  syn match eggRepeatModifier	     "\<\%(while\)\>" display

  syn region eggDoBlock      matchgroup=eggControl start="\<do\>" end="\<end\>"                 contains=ALLBUT,@eggNotTop fold
  " curly bracket block or hash literal
  syn region eggCurlyBlock	matchgroup=eggCurlyBlockDelimiter  start="{" end="}"				contains=ALLBUT,@eggNotTop fold

  " statements without 'do'
  syn region eggBlockExpression       matchgroup=eggContro	start="\<begin\>" end="\<end\>" contains=ALLBUT,@eggNotTop fold
  syn region eggCaseExpression	      matchgroup=eggConditional start="\<match\>"  end="\<end\>" contains=ALLBUT,@eggNotTop fold
  syn region eggConditionalExpression matchgroup=eggConditional start="\%(\%(^\|\.\.\.\=\|[{:,;([<>~\*/%&^|+=-]\|\%(\<[_[:lower:]][_[:alnum:]]*\)\@<![?!]\)\s*\)\@<=\%(if\|unless\)\>" end="\%(\%(\%(\.\@<!\.\)\|::\)\s*\)\@<!\<end\>" contains=ALLBUT,@eggNotTop fold

  syn match eggConditional "\<\%(then\|else\|with\)\>[?!]\@!"	contained containedin=eggCaseExpression
  syn match eggConditional "\<\%(then\|else\|elseif\)\>[?!]\@!" contained containedin=eggConditionalExpression

  syn match eggExceptional	  "\<\%(\%(\%(;\|^\)\s*\)\@<=do\|exception\|end\)\>[?!]\@!" contained containedin=eggBlockExpression

  if !exists("egg_minlines")
    let egg_minlines = 500
  endif
  exec "syn sync minlines=" . egg_minlines
  syn match eggControl "\<\%(begin\|do\|if\|then\|else\|elseif\|exception\|with\|match\|end\)\>[?!]\@!"
endif

" Comments and Documentation
syn keyword eggTodo	  FIXME NOTE TODO OPTIMIZE XXX todo contained
syn match   eggComment   "--.*" contains=eggTodo,@Spell
if !exists("egg_no_comment_fold")
   syn region eggMultilineComment start="-[*]" end="[*]-" contains=eggTodo,@Spell transparent fold keepend
 endif

hi def link eggFunction			Function
hi def link eggConditional		Conditional
hi def link eggConditionalModifier	eggConditional
hi def link eggExceptional		eggConditional
hi def link eggControl			Statement
hi def link eggInteger			Number
hi def link eggASCIICode		Character
hi def link eggFloat			Float
hi def link eggBoolean			Boolean
if !exists("egg_no_identifiers")
  hi def link eggIdentifier		Identifier
else
  hi def link eggIdentifier		NONE
endif
hi def link eggFn			eggIdentifier
hi def link eggConstant			Type
hi def link eggSymbol			Constant
hi def link eggKeyword			Keyword
hi def link eggOperator			Operator
hi def link eggBeginEnd			Statement
hi def link eggAccess			Statement
hi def link eggAttribute		Statement
hi def link eggEval			Statement
hi def link eggPseudoVariable		Constant

hi def link eggComment			Comment
hi def link eggMultilineComment		Comment
hi def link eggData			Comment
hi def link eggDataDirective		Delimiter
hi def link eggDocumentation		Comment
hi def link eggTodo			Todo

hi def link eggQuoteEscape		eggStringEscape
hi def link eggStringEscape		Special
hi def link eggInterpolationDelimiter	Delimiter
hi def link eggNoInterpolation		eggString
hi def link eggSharpBang		PreProc
hi def link eggRegexpDelimiter		eggStringDelimiter
hi def link eggSymbolDelimiter		eggStringDelimiter
hi def link eggStringDelimiter		Delimiter
hi def link eggString			String

hi def link eggError			Error
hi def link eggSpaceError		eggError

let b:current_syntax = "egg"

" vim: nowrap sw=2 sts=2 ts=8 noet:
