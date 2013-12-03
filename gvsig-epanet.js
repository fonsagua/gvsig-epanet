var $j = jQuery.noConflict();

var m_px;
var max_level;

function levelling(level) {
    if ( level < max_level ) {
        level = max_level;
        $j('#level').fadeOut('fast');
        $j('#level-fixed').fadeIn('fast');
    } else {
        $j('#level').fadeIn('fast');
        $j('#level-fixed').fadeOut('fast');
    };
    $j('#air').animate({ height: level }, {
        step: function( now, fx ) {
            var level_m = 26 - now * m_px;
            $j("#leveltxt").html(level_m.toFixed(1));
        }}, 'fast');
};

$j(window).scroll(function () {
    $j('#nivel').hide();
    var nulevel = $j(window).scrollTop();
    $j.doTimeout( 'scroll', 250, function(){
        nulevel = $j(window).scrollTop() + 50;
        levelling(nulevel);
        $j('#nivel').fadeIn(1200);
    });
});

$j(document).ready(function(){
    var winh = $j('#contenido').height();
    m_px = 25 / winh;
    max_level = $j('#contenidos').offset().top;
});
