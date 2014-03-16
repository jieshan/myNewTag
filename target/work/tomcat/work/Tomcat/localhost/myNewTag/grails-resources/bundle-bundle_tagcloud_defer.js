/**
 * Copyright (C) 2010-2013 Graham Breach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * jQuery.tagcanvas 2.2
 * For more information, please contact <graham@goat1000.com>
 */
(function($){
"use strict";
var i, j, abs = Math.abs, sin = Math.sin, cos = Math.cos, max = Math.max,
  min = Math.min, ceil = Math.ceil, sqrt = Math.sqrt, pow = Math.pow,
  hexlookup3 = {}, hexlookup2 = {}, hexlookup1 = {
  0:"0,",   1:"17,",  2:"34,",  3:"51,",  4:"68,",  5:"85,",
  6:"102,", 7:"119,", 8:"136,", 9:"153,", a:"170,", A:"170,",
  b:"187,", B:"187,", c:"204,", C:"204,", d:"221,", D:"221,",
  e:"238,", E:"238,", f:"255,", F:"255,"  
  }, Oproto, Tproto, TCproto, Mproto, Vproto, TSproto, doc = document, ocanvas, 
  handlers = {};
for(i = 0; i < 256; ++i) {
  j = i.toString(16);
  if(i < 16)
    j = '0' + j;
  hexlookup2[j] = hexlookup2[j.toUpperCase()] = i.toString() + ',';
}
function Defined(d) {
  return typeof d != 'undefined';
}
function IsObject(o) {
  return typeof o == 'object' && o != null;
}
function Clamp(v, mn, mx) {
  return isNaN(v) ? mx : min(mx, max(mn, v));
}
function Nop() {
  return false;
}
function TimeNow() {
  return new Date().valueOf();
}
function SortList(l, f) {
  var nl = [], tl = l.length, i;
  for(i = 0; i < tl; ++i)
    nl.push(l[i]);
  nl.sort(f);
  return nl;
}
function Shuffle(a) {
  var i = a.length-1, t, p;
  while(i) {
    p = ~~(Math.random()*i);
    t = a[i];
    a[i] = a[p];
    a[p] = t;
    --i;
  }
}
function Vector(x, y, z) {
  this.x = x;
  this.y = y;
  this.z = z;
}
Vproto = Vector.prototype;
Vproto.length = function() {
  return sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
};
Vproto.dot = function(v) {
  return this.x * v.x + this.y * v.y + this.z * v.z;
};
Vproto.cross = function(v) {
  var x = this.y * v.z - this.z * v.y,
    y = this.z * v.x - this.x * v.z,
    z = this.x * v.y - this.y * v.x;
  return new Vector(x, y, z);
};
Vproto.angle = function(v) {
  var dot = this.dot(v), ac;
  if(dot == 0)
    return Math.PI / 2.0;
  ac = dot / (this.length() * v.length());
  if(ac >= 1)
    return 0;
  if(ac <= -1)
    return Math.PI;
  return Math.acos(ac);
};
Vproto.unit = function() {
  var l = this.length();
  return new Vector(this.x / l, this.y / l, this.z / l);
};
function MakeVector(lg, lt) {
  lt = lt * Math.PI / 180;
  lg = lg * Math.PI / 180;
  var x = sin(lg) * cos(lt), y = -sin(lt), z = -cos(lg) * cos(lt);
  return new Vector(x, y, z);
}
function Matrix(a) {
  this[1] = {1: a[0],  2: a[1],  3: a[2]};
  this[2] = {1: a[3],  2: a[4],  3: a[5]};
  this[3] = {1: a[6],  2: a[7],  3: a[8]};
}
Mproto = Matrix.prototype;
Matrix.Identity = function() {
  return new Matrix([1,0,0, 0,1,0, 0,0,1]);
};
Matrix.Rotation = function(angle, u) {
  var sina = sin(angle), cosa = cos(angle), mcos = 1 - cosa;
  return new Matrix([
    cosa + pow(u.x, 2) * mcos, u.x * u.y * mcos - u.z * sina, u.x * u.z * mcos + u.y * sina,
    u.y * u.x * mcos + u.z * sina, cosa + pow(u.y, 2) * mcos, u.y * u.z * mcos - u.x * sina,
    u.z * u.x * mcos - u.y * sina, u.z * u.y * mcos + u.x * sina, cosa + pow(u.z, 2) * mcos
  ]);
}
Mproto.mul = function(m) {
  var a = [], i, j, mmatrix = (m.xform ? 1 : 0);
  for(i = 1; i <= 3; ++i)
    for(j = 1; j <= 3; ++j) {
      if(mmatrix)
        a.push(this[i][1] * m[1][j] +
          this[i][2] * m[2][j] +
          this[i][3] * m[3][j]);
      else
        a.push(this[i][j] * m);
    }
  return new Matrix(a);
};
Mproto.xform = function(p) {
  var a = {}, x = p.x, y = p.y, z = p.z;
  a.x = x * this[1][1] + y * this[2][1] + z * this[3][1];
  a.y = x * this[1][2] + y * this[2][2] + z * this[3][2];
  a.z = x * this[1][3] + y * this[2][3] + z * this[3][3];
  return a;
};
function PointsOnSphere(n,xr,yr,zr) {
  var i, y, r, phi, pts = [], inc = Math.PI * (3-sqrt(5)), off = 2/n;
  for(i = 0; i < n; ++i) {
    y = i * off - 1 + (off / 2);
    r = sqrt(1 - y*y);
    phi = i * inc;
    pts.push([cos(phi) * r * xr, y * yr, sin(phi) * r * zr]);
  }
  return pts;
}
function Cylinder(n,o,xr,yr,zr) {
  var phi, pts = [], inc = Math.PI * (3-sqrt(5)), off = 2/n, i, j, k, l;
  for(i = 0; i < n; ++i) {
    j = i * off - 1 + (off / 2);
    phi = i * inc;
    k = cos(phi);
    l = sin(phi);
    pts.push(o ? [j * xr, k * yr, l * zr] : [k * xr, j * yr, l * zr]);
  }
  return pts;
}
function Ring(o, n, xr, yr, zr, j) {
  var phi, pts = [], inc = Math.PI * 2 / n, i, k, l;
  for(i = 0; i < n; ++i) {
    phi = i * inc;
    k = cos(phi);
    l = sin(phi);
    pts.push(o ? [j * xr, k * yr, l * zr] : [k * xr, j * yr, l * zr]);
  }
  return pts;
}
function PointsOnCylinderV(n,xr,yr,zr) { return Cylinder(n, 0, xr, yr, zr) }
function PointsOnCylinderH(n,xr,yr,zr) { return Cylinder(n, 1, xr, yr, zr) }
function PointsOnRingV(n, xr, yr, zr, offset) {
  offset = isNaN(offset) ? 0 : offset * 1;
  return Ring(0, n, xr, yr, zr, offset);
}
function PointsOnRingH(n, xr, yr, zr, offset) {
  offset = isNaN(offset) ? 0 : offset * 1;
  return Ring(1, n, xr, yr, zr, offset);
}
function SetAlpha(c,a) {
  var d = c, p1, p2, ae = (a*1).toPrecision(3) + ')';
  if(c[0] === '#') {
    if(!hexlookup3[c])
      if(c.length === 4)
        hexlookup3[c] = 'rgba(' + hexlookup1[c[1]] + hexlookup1[c[2]] + hexlookup1[c[3]];
      else
        hexlookup3[c] = 'rgba(' + hexlookup2[c.substr(1,2)] + hexlookup2[c.substr(3,2)] + hexlookup2[c.substr(5,2)];
    d = hexlookup3[c] + ae;
  } else if(c.substr(0,4) === 'rgb(' || c.substr(0,4) === 'hsl(') {
    d = (c.replace('(','a(').replace(')', ',' + ae));
  } else if(c.substr(0,5) === 'rgba(' || c.substr(0,5) === 'hsla(') {
    p1 = c.lastIndexOf(',') + 1, p2 = c.indexOf(')');
    a *= parseFloat(c.substring(p1,p2));
    d = c.substr(0,p1) + a.toPrecision(3) + ')';
  }
  return d;
}
function NewCanvas(w,h) {
  // if using excanvas, give up now
  if(window.G_vmlCanvasManager)
    return null;
  var c = doc.createElement('canvas');
  c.width = w;
  c.height = h;
  return c;
}
// I think all browsers pass this test now...
function ShadowAlphaBroken() {
  var cv = NewCanvas(3,3), c, i;
  if(!cv)
    return false;
  c = cv.getContext('2d');
  c.strokeStyle = '#000';
  c.shadowColor = '#fff';
  c.shadowBlur = 3;
  c.globalAlpha = 0;
  c.strokeRect(2,2,2,2);
  c.globalAlpha = 1;
  i = c.getImageData(2,2,1,1);
  cv = null;
  return (i.data[0] > 0);
}
function FindGradientColour(t,p) {
  var l = 1024, g = t.weightGradient, cv, c, i, gd, d;
  if(t.gCanvas) {
    c = t.gCanvas.getContext('2d');
  } else {
    t.gCanvas = cv = NewCanvas(l,1);
    if(!cv)
      return null;
    c = cv.getContext('2d');
    gd = c.createLinearGradient(0,0,l,0);
    for(i in g)
      gd.addColorStop(1-i, g[i]);
    c.fillStyle = gd;
    c.fillRect(0,0,l,1);
  }
  d = c.getImageData(~~((l-1)*p),0,1,1).data;
  return 'rgba(' + d[0] + ',' + d[1] + ',' + d[2] + ',' + (d[3]/255) + ')';
}
function TextSet(c,f,l,s,sc,sb,so,wm,wl) {
  var xo = (sb || 0) + (so && so[0] < 0 ? abs(so[0]) : 0),
    yo = (sb || 0) + (so && so[1] < 0 ? abs(so[1]) : 0), i, xc;
  c.font = f;
  c.textBaseline = 'top';
  c.fillStyle = l;
  sc && (c.shadowColor = sc);
  sb && (c.shadowBlur = sb);
  so && (c.shadowOffsetX = so[0], c.shadowOffsetY = so[1]);
  for(i = 0; i < s.length; ++i) {
    xc = wl ? (wm - wl[i]) / 2 : 0;
    c.fillText(s[i], xo + xc, yo);
    yo += parseInt(f);
  }
}
function TextToCanvas(s,f,ht,w,h,l,sc,sb,so,padx,pady,wmax,wlist) {
  var cw = w + abs(so[0]) + sb + sb, ch = h + abs(so[1]) + sb + sb, cv, c;
  cv = NewCanvas(cw+padx,ch+pady);
  if(!cv)
    return null;
  c = cv.getContext('2d');
  TextSet(c,f,l,s,sc,sb,so,wmax,wlist);
  return cv;
}
function AddShadowToImage(i,sc,sb,so) {
  var sw = abs(so[0]), sh = abs(so[1]),
    cw = i.width + (sw > sb ? sw + sb : sb * 2),
    ch = i.height + (sh > sb ? sh + sb : sb * 2),
    xo = (sb || 0) + (so[0] < 0 ? sw : 0),
    yo = (sb || 0) + (so[1] < 0 ? sh : 0), cv, c;
  cv = NewCanvas(cw, ch);
  if(!cv)
    return null;
  c = cv.getContext('2d');
  sc && (c.shadowColor = sc);
  sb && (c.shadowBlur = sb);
  so && (c.shadowOffsetX = so[0], c.shadowOffsetY = so[1]);
  c.drawImage(i, xo, yo, i.width, i.height);
  return cv;
}
function FindTextBoundingBox(s,f,ht) {
  var w = parseInt(s.toString().length * ht), h = parseInt(ht * 2 * s.length),
    cv = NewCanvas(w,h), c, idata, w1, h1, x, y, i, ex;
  if(!cv)
    return null;
  c = cv.getContext('2d');
  c.fillStyle = '#000';
  c.fillRect(0,0,w,h);
  TextSet(c,ht + 'px ' + f,'#fff',s,0,0,[])

  idata = c.getImageData(0,0,w,h);
  w1 = idata.width; h1 = idata.height;
  ex = {
    min: { x: w1, y: h1 },
    max: { x: -1, y: -1 }
  };
  for(y = 0; y < h1; ++y) {
    for(x = 0; x < w1; ++x) {
      i = (y * w1 + x) * 4;
      if(idata.data[i+1] > 0) {
        if(x < ex.min.x) ex.min.x = x;
        if(x > ex.max.x) ex.max.x = x;
        if(y < ex.min.y) ex.min.y = y;
        if(y > ex.max.y) ex.max.y = y;
      }
    }
  }
  // device pixels might not be css pixels
  if(w1 != w) {
    ex.min.x *= (w / w1);
    ex.max.x *= (w / w1);
  }
  if(h1 != h) {
    ex.min.y *= (w / h1);
    ex.max.y *= (w / h1);
  }

  cv = null;
  return ex;
}
function FixFont(f) {
  return "'" + f.replace(/(\'|\")/g,'').replace(/\s*,\s*/g, "', '") + "'";
}
function AddHandler(h,f,e) {
  e = e || doc;
  if(e.addEventListener)
    e.addEventListener(h,f,false);
  else
    e.attachEvent('on' + h, f);
}
function AddImage(i, o, t, tc) {
  var s = tc.imageScale, ic;
  // image not loaded, wait for image onload
  if(!o.complete)
    return AddHandler('load',function() { AddImage(i,o,t,tc); }, o);
  if(!i.complete)
    return AddHandler('load',function() { AddImage(i,o,t,tc); }, i);

  // Yes, this does look like nonsense, but it makes sure that both the
  // width and height are actually set and not just calculated. This is
  // required to keep proportional sizes when the images are hidden, so
  // the images can be used again for another cloud.
  o.width = o.width;
  o.height = o.height;

  if(s) {
    i.width = o.width * s;
    i.height = o.height * s;
  }
  t.w = i.width;
  t.h = i.height;
  if(tc.txtOpt && tc.shadow) {
    ic = AddShadowToImage(i, tc.shadow, tc.shadowBlur, tc.shadowOffset);
    if(ic) {
      t.image = ic;
      t.w = ic.width;
      t.h = ic.height;
    }
  }
}
function GetProperty(e,p) {
  var dv = doc.defaultView, pc = p.replace(/\-([a-z])/g,function(a){return a.charAt(1).toUpperCase()});
  return (dv && dv.getComputedStyle && dv.getComputedStyle(e,null).getPropertyValue(p)) ||
    (e.currentStyle && e.currentStyle[pc]);
}
function FindWeight(t,a) {
  var w = 1, p;
  if(t.weightFrom) {
    w = 1 * (a.getAttribute(t.weightFrom) || t.textHeight);
  } else if(p = GetProperty(a,'font-size')) {
    w = (p.indexOf('px') > -1 && p.replace('px','') * 1) ||
      (p.indexOf('pt') > -1 && p.replace('pt','') * 1.25) ||
      p * 3.3;
  } else {
    t.weight = false;
  }
  return w;
}
function EventToCanvasId(e) {
  return e.target && Defined(e.target.id) ? e.target.id :
    e.srcElement.parentNode.id;
}
function EventXY(e, c) {
  var xy, p, xmul = parseInt(GetProperty(c, 'width')) / c.width,
    ymul = parseInt(GetProperty(c, 'height')) / c.height;
  if(Defined(e.offsetX)) {
    xy = {x: e.offsetX, y: e.offsetY};
  } else {
    p = AbsPos(c.id);
    if(Defined(e.changedTouches))
      e = e.changedTouches[0];
    if(e.pageX)
      xy = {x: e.pageX - p.x, y: e.pageY - p.y};
  }
  if(xy && xmul && ymul) {
    xy.x /= xmul;
    xy.y /= ymul;
  }
  return xy;
}
function MouseOut(e) {
  var cv = e.target || e.fromElement.parentNode, tc = TagCanvas.tc[cv.id];
  if(tc) {
   tc.mx = tc.my = -1;
   tc.UnFreeze();
   tc.EndDrag();
  }
}
function MouseMove(e) {
  var i, t = TagCanvas, tc, p, tg = EventToCanvasId(e);
  for(i in t.tc) {
    tc = t.tc[i];
    if(tc.tttimer) {
      clearTimeout(tc.tttimer);
      tc.tttimer = null;
    }
  }
  if(tg && t.tc[tg]) {
    tc = t.tc[tg];
    if(p = EventXY(e, tc.canvas)) {
      tc.mx = p.x;
      tc.my = p.y;
      tc.Drag(e, p);
    }
    tc.drawn = 0;
  }
}
function MouseDown(e) {
  var t = TagCanvas, cb = doc.addEventListener ? 0 : 1,
    tg = EventToCanvasId(e);
  if(tg && e.button == cb && t.tc[tg]) {
    t.tc[tg].BeginDrag(e);
  }
}
function MouseUp(e) {
  var t = TagCanvas, cb = doc.addEventListener ? 0 : 1,
    tg = EventToCanvasId(e), tc;
  if(tg && e.button == cb && t.tc[tg]) {
    tc = t.tc[tg];
    MouseMove(e);
    if(!tc.EndDrag() && !tc.touched)
      tc.Clicked(e);
  }
}
function TouchDown(e) {
  var t = TagCanvas, tg = EventToCanvasId(e);
  if(tg && e.changedTouches && t.tc[tg]) {
    t.tc[tg].touched = 1;
    t.tc[tg].BeginDrag(e);
  }
}
function TouchUp(e) {
  var t = TagCanvas, tg = EventToCanvasId(e);
  if(tg && e.changedTouches && t.tc[tg]) {
    TouchMove(e);
    if(!t.tc[tg].EndDrag()){
      t.tc[tg].Draw();
      t.tc[tg].Clicked(e);
    }
  }
}
function TouchMove(e) {
  var i, t = TagCanvas, tc, p, tg = EventToCanvasId(e);
  for(i in t.tc) {
    tc = t.tc[i];
    if(tc.tttimer) {
      clearTimeout(tc.tttimer);
      tc.tttimer = null;
    }
  }
  if(tg && t.tc[tg] && e.changedTouches) {
    tc = t.tc[tg];
    if(p = EventXY(e, tc.canvas)) {
      tc.mx = p.x;
      tc.my = p.y;
      tc.Drag(e, p);
    }
    tc.drawn = 0;
  }
}
function MouseWheel(e) {
  var t = TagCanvas, tg = EventToCanvasId(e);
  if(tg && t.tc[tg]) {
    e.cancelBubble = true;
    e.returnValue = false;
    e.preventDefault && e.preventDefault();
    t.tc[tg].Wheel((e.wheelDelta || e.detail) > 0);
  }
}
function DrawCanvas(t) {
  var tc = TagCanvas.tc, i, interval;
  t = t || TimeNow();
  for(i in tc) {
    interval = tc[i].interval;
    tc[i].Draw(t);
  }
  TagCanvas.NextFrame(interval);
}
function AbsPos(id) {
  var e = doc.getElementById(id), r = e.getBoundingClientRect(),
    dd = doc.documentElement, b = doc.body, w = window,
    xs = w.pageXOffset || dd.scrollLeft,
    ys = w.pageYOffset || dd.scrollTop,
    xo = dd.clientLeft || b.clientLeft,
    yo = dd.clientTop || b.clientTop;
  return { x: r.left + xs - xo, y: r.top + ys - yo };
}
function Project(tc,p1,sx,sy) {
  var m = tc.radius * tc.z1 / (tc.z1 + tc.z2 + p1.z);
  return {
    x: p1.x * m * sx,
    y: p1.y * m * sy,
    z: p1.z,
    w: (tc.z1 - p1.z) / tc.z2
  };
}
/**
 * @constructor
 * for recursively splitting tag contents on <br> tags
 */
function TextSplitter(e) {
  this.e = e;
  this.br = 0;
  this.line = [];
  this.text = [];
  this.original = e.innerText || e.textContent;
}
TSproto = TextSplitter.prototype;
TSproto.Lines = function(e) {
  var r = e ? 1 : 0, cn, cl, i;
  e = e || this.e;
  cn = e.childNodes;
  cl = cn.length;

  for(i = 0; i < cl; ++i) {
    if(cn[i].nodeName == 'BR') {
      this.text.push(this.line.join(' '));
      this.br = 1;
    } else if(cn[i].nodeType == 3) {
      if(this.br) {
        this.line = [cn[i].nodeValue];
        this.br = 0;
      } else {
        this.line.push(cn[i].nodeValue);
      }
    } else {
      this.Lines(cn[i]);
    }
  }
  r || this.br || this.text.push(this.line.join(' '));
  return this.text;
}
TSproto.SplitWidth = function(w, c, f, h) {
  var i, j, words, text = [];
  c.font = h + 'px ' + f;
  for(i = 0; i < this.text.length; ++i) {
    words = this.text[i].split(/\s+/);
    this.line = [words[0]];
    for(j = 1; j < words.length; ++j) {
      if(c.measureText(this.line.join(' ') + ' ' + words[j]).width > w) {
        text.push(this.line.join(' '));
        this.line = [words[j]];
      } else {
        this.line.push(words[j]);
      }
    }
    text.push(this.line.join(' '));
  }
  return this.text = text;
}
/**
 * @constructor
 */
function Outline(tc) {
  this.ts = TimeNow();
  this.tc = tc;
  this.x = this.y = this.w = this.h = this.sc = 1;
  this.z = 0;
  this.Draw = tc.pulsateTo < 1 && tc.outlineMethod != 'colour' ? this.DrawPulsate : this.DrawSimple;
  this.SetMethod(tc.outlineMethod);
}
Oproto = Outline.prototype;
Oproto.SetMethod = function(om) {
  var methods = {
    block: ['PreDraw','DrawBlock'],
    colour: ['PreDraw','DrawColour'],
    outline: ['PostDraw','DrawOutline'],
    classic: ['LastDraw','DrawOutline'],
    none: ['LastDraw']
  }, funcs = methods[om] || methods.outline;
  if(om == 'none') {
    this.Draw = function() { return 1; }
  } else {
    this.drawFunc = this[funcs[1]];
  }
  this[funcs[0]] = this.Draw;
};
Oproto.Update = function(x,y,w,h,sc,z,xo,yo) {
  var o = this.tc.outlineOffset, o2 = 2 * o;
  this.x = sc * x + xo - o;
  this.y = sc * y + yo - o;
  this.w = sc * w + o2;
  this.h = sc * h + o2;
  this.sc = sc; // used to determine frontmost
  this.z = z;
};
Oproto.DrawOutline = function(c,x,y,w,h,colour) {
  c.strokeStyle = colour;
  c.strokeRect(x,y,w,h);
};
Oproto.DrawColour = function(c,x,y,w,h,colour,tag,x1,y1) {
  return this[tag.image ? 'DrawColourImage' : 'DrawColourText'](c,x,y,w,h,colour,tag,x1,y1);
};
Oproto.DrawColourText = function(c,x,y,w,h,colour,tag,x1,y1) {
  var normal = tag.colour;
  tag.colour = colour;
  tag.alpha = 1;
  tag.Draw(c,x1,y1);
  tag.colour = normal;
  return 1;
};
Oproto.DrawColourImage = function(c,x,y,w,h,colour,tag,x1,y1) {
  var ccanvas = c.canvas, fx = ~~max(x,0), fy = ~~max(y,0), 
    fw = min(ccanvas.width - fx, w) + .5|0, fh = min(ccanvas.height - fy,h) + .5|0, cc;
  if(ocanvas)
    ocanvas.width = fw, ocanvas.height = fh;
  else
    ocanvas = NewCanvas(fw, fh);
  if(!ocanvas)
    return this.SetMethod('outline'); // if using IE and images, give up!
  cc = ocanvas.getContext('2d');

  cc.drawImage(ccanvas,fx,fy,fw,fh,0,0,fw,fh);
  c.clearRect(fx,fy,fw,fh);
  tag.alpha = 1;
  tag.Draw(c,x1,y1);
  c.setTransform(1,0,0,1,0,0);
  c.save();
  c.beginPath();
  c.rect(fx,fy,fw,fh);
  c.clip();
  c.globalCompositeOperation = 'source-in';
  c.fillStyle = colour;
  c.fillRect(fx,fy,fw,fh);
  c.restore();
  c.globalCompositeOperation = 'destination-over';
  c.drawImage(ocanvas,0,0,fw,fh,fx,fy,fw,fh);
  c.globalCompositeOperation = 'source-over';
  return 1;
};
Oproto.DrawBlock = function(c,x,y,w,h,colour) {
  c.fillStyle = colour;
  c.fillRect(x,y,w,h);
};
Oproto.DrawSimple = function(c, tag, x1, y1) {
  var t = this.tc;
  c.setTransform(1,0,0,1,0,0);
  c.strokeStyle = t.outlineColour;
  c.lineWidth = t.outlineThickness;
  c.shadowBlur = c.shadowOffsetX = c.shadowOffsetY = 0;
  c.globalAlpha = 1;
  return this.drawFunc(c,this.x,this.y,this.w,this.h,t.outlineColour,tag,x1,y1);
};
Oproto.DrawPulsate = function(c, tag, x1, y1) {
  var diff = TimeNow() - this.ts, t = this.tc;
  c.setTransform(1,0,0,1,0,0);
  c.strokeStyle = t.outlineColour;
  c.lineWidth = t.outlineThickness;
  c.shadowBlur = c.shadowOffsetX = c.shadowOffsetY = 0;
  c.globalAlpha = t.pulsateTo + ((1 - t.pulsateTo) * 
    (0.5 + (cos(2 * Math.PI * diff / (1000 * t.pulsateTime)) / 2)));
  return this.drawFunc(c,this.x,this.y,this.w,this.h,t.outlineColour,tag,x1,y1);
};
Oproto.Active = function(c,x,y) {
  return (x >= this.x && y >= this.y &&
    x <= this.x + this.w && y <= this.y + this.h);
};
Oproto.PreDraw = Oproto.PostDraw = Oproto.LastDraw = Nop;
/**
 * @constructor
 */
function Tag(tc,text,a,v,w,h,col,font,original) {
  var c = tc.ctxt;
  this.tc = tc;
  this.image = text.src ? text : null;
  this.text = text.src ? [] : text;
  this.text_original = original;
  this.line_widths = [];
  this.title = a.title || null;
  this.a = a;
  this.position = new Vector(v[0], v[1], v[2]);
  this.x = this.y = this.z = 0;
  this.w = w;
  this.h = h;
  this.colour = col || tc.textColour;
  this.textFont = font || tc.textFont;
  this.weight = this.sc = this.alpha = 1;
  this.weighted = !tc.weight;
  this.outline = new Outline(tc);
  if(!this.image) {
    this.textHeight = tc.textHeight;
    this.extents = FindTextBoundingBox(this.text, this.textFont, this.textHeight);
    this.Measure(c,tc);
  }
  this.SetShadowColour = tc.shadowAlpha ? this.SetShadowColourAlpha : this.SetShadowColourFixed;
  this.SetDraw(tc);
}
Tproto = Tag.prototype;
Tproto.EqualTo = function(e) {
  var i = e.getElementsByTagName('img');
  if(this.a.href != e.href)
    return 0;
  if(i.length)
    return this.image.src == i[0].src;
  return (e.innerText || e.textContent) == this.text_original;
};
Tproto.SetDraw = function(t) {
  this.Draw = this.image ? (t.ie > 7 ? this.DrawImageIE : this.DrawImage) : this.DrawText;
  t.noSelect && (this.CheckActive = Nop);
};
Tproto.MeasureText = function(c) {
  var i, l = this.text.length, w = 0, wl;
  for(i = 0; i < l; ++i) {
    this.line_widths[i] = wl = c.measureText(this.text[i]).width;
    w = max(w, wl);
  }
  return w;
};
Tproto.Measure = function(c,t) {
  this.h = this.extents ? this.extents.max.y + this.extents.min.y : this.textHeight;
  c.font = this.font = this.textHeight + 'px ' + this.textFont;
  this.w = this.MeasureText(c);
  if(t.txtOpt) {
    var s = t.txtScale, th = s * this.textHeight, f = th + 'px ' + this.textFont,
      soff = [s*t.shadowOffset[0],s*t.shadowOffset[1]], cw;
    c.font = f;
    cw = this.MeasureText(c);
    this.image = TextToCanvas(this.text, f, th, cw, s * this.h, this.colour,
      t.shadow, s * t.shadowBlur, soff, s, s, cw, this.line_widths);
    if(this.image) {
      this.w = this.image.width / s;
      this.h = this.image.height / s;
    }
    this.SetDraw(t);
    t.txtOpt = !!this.image;
  }
};
Tproto.SetFont = function(f, c) {
  this.textFont = f;
  this.colour = c;
  this.extents = FindTextBoundingBox(this.text, this.textFont, this.textHeight);
  this.Measure(this.tc.ctxt, this.tc);
};
Tproto.SetWeight = function(w) {
  if(!this.text.length)
    return;
  this.weight = w;
  this.Weight(this.tc.ctxt, this.tc);
  this.Measure(this.tc.ctxt, this.tc);
};
Tproto.Weight = function(c,t) {
  var w = this.weight, m = t.weightMode;
  this.weighted = true;
  if(m == 'colour' || m == 'both')
    this.colour = FindGradientColour(t, (w - t.min_weight) / (t.max_weight-t.min_weight));
  if(m == 'size' || m == 'both') {
    if(t.weightSizeMin > 0 && t.weightSizeMax > t.weightSizeMin) {
      this.textHeight = t.weightSize * 
        (t.weightSizeMin + (t.weightSizeMax - t.weightSizeMin) *
         (w - t.min_weight) / (t.max_weight - t.min_weight));
    } else {
      this.textHeight = w * t.weightSize;
    }
  }
  this.extents = FindTextBoundingBox(this.text, this.textFont, this.textHeight);
};
Tproto.SetShadowColourFixed = function(c,s,a) {
  c.shadowColor = s;
};
Tproto.SetShadowColourAlpha = function(c,s,a) {
  c.shadowColor = SetAlpha(s, a);
};
Tproto.DrawText = function(c,xoff,yoff) {
  var t = this.tc, x = this.x, y = this.y, s = this.sc, i, xl;
  c.globalAlpha = this.alpha;
  c.fillStyle = this.colour;
  t.shadow && this.SetShadowColour(c,t.shadow,this.alpha);
  c.font = this.font;
  x += xoff / s;
  y += (yoff / s) - (this.h / 2);
  for(i = 0; i < this.text.length; ++i) {
    xl = x - (this.line_widths[i] / 2);
    c.setTransform(s, 0, 0, s, s * xl, s * y);
    c.fillText(this.text[i], 0, 0);
    y += this.textHeight;
  }
};
Tproto.DrawImage = function(c,xoff,yoff) {
  var x = this.x, y = this.y, s = this.sc,
    i = this.image, w = this.w, h = this.h, a = this.alpha,
    shadow = this.shadow;
  c.globalAlpha = a;
  shadow && this.SetShadowColour(c,shadow,a);
  x += (xoff / s) - (w / 2);
  y += (yoff / s) - (h / 2);
  c.setTransform(s, 0, 0, s, s * x, s * y);
  c.drawImage(i, 0, 0, w, h);
};
Tproto.DrawImageIE = function(c,xoff,yoff) {
  var i = this.image, s = this.sc,
    w = i.width = this.w*s, h = i.height = this.h * s,
    x = (this.x*s) + xoff - (w/2), y = (this.y*s) + yoff - (h/2);
  c.setTransform(1,0,0,1,0,0);
  c.globalAlpha = this.alpha;
  c.drawImage(i, x, y);
};
Tproto.Calc = function(m,a) {
  var pp, t = this.tc, mnb = t.minBrightness,
    mxb = t.maxBrightness, r = t.max_radius;
  pp = m.xform(this.position);
  this.xformed = pp;
  pp = Project(t, pp, t.stretchX, t.stretchY);
  this.x = pp.x;
  this.y = pp.y;
  this.z = pp.z;
  this.sc = pp.w;
  this.alpha = a * Clamp(mnb + (mxb - mnb) * (r - this.z) / (2 * r), 0, 1);
};
Tproto.CheckActive = function(c,xoff,yoff) {
  var t = this.tc, o = this.outline,
    w = this.w, h = this.h,
    x = this.x - w/2, y = this.y - h/2;
  o.Update(x, y, w, h, this.sc, this.z, xoff, yoff);
  return o.Active(c, t.mx, t.my) ? o : null;
};
Tproto.Clicked = function(e) {
  var a = this.a, t = a.target, h = a.href, evt;
  if(t != '' && t != '_self') {
    if(self.frames[t]) {
      self.frames[t].document.location = h;
    } else{
      try {
        if(top.frames[t]) {
          top.frames[t].document.location = h;
          return;
        }
      } catch(err) {
        // different domain/port/protocol?
      }
      window.open(h, t);
    }
    return;
  }
  if(doc.createEvent) {
    evt = doc.createEvent('MouseEvents');
    evt.initMouseEvent('click', 1, 1, window, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    if(!a.dispatchEvent(evt))
      return;
  } else if(a.fireEvent) {
    if(!a.fireEvent('onclick'))
      return;
  }
  doc.location = h;
};
/**
 * @constructor
 */
function TagCanvas(cid,lctr,opt) {
  var i, p, c = doc.getElementById(cid), cp = ['id','class','innerHTML'];

  if(!c) throw 0;
  if(Defined(window.G_vmlCanvasManager)) {
    c = window.G_vmlCanvasManager.initElement(c);
    this.ie = parseFloat(navigator.appVersion.split('MSIE')[1]);
  }
  if(c && (!c.getContext || !c.getContext('2d').fillText)) {
    p = doc.createElement('DIV');
    for(i = 0; i < cp.length; ++i)
      p[cp[i]] = c[cp[i]];
    c.parentNode.insertBefore(p,c);
    c.parentNode.removeChild(c);
    throw 0;
  }
  for(i in TagCanvas.options)
    this[i] = opt && Defined(opt[i]) ? opt[i] : 
      (Defined(TagCanvas[i]) ? TagCanvas[i] : TagCanvas.options[i]);

  this.canvas = c;
  this.ctxt = c.getContext('2d');
  this.z1 = 250 / this.depth;
  this.z2 = this.z1 / this.zoom;
  this.radius = min(c.height, c.width) * 0.0075; // fits radius of 100 in canvas
  this.max_weight = 0;
  this.min_weight = 200;
  this.textFont = this.textFont && FixFont(this.textFont);
  this.textHeight *= 1;
  this.pulsateTo = Clamp(this.pulsateTo, 0, 1);
  this.minBrightness = Clamp(this.minBrightness, 0, 1);
  this.maxBrightness = Clamp(this.maxBrightness, this.minBrightness, 1);
  this.ctxt.textBaseline = 'top';
  this.lx = (this.lock + '').indexOf('x') + 1;
  this.ly = (this.lock + '').indexOf('y') + 1;
  this.frozen = this.dx = this.dy = this.fixedAnim = this.touched = 0;
  this.fixedAlpha = 1;
  this.source = lctr || cid;
  this.transform = Matrix.Identity();
  this.startTime = this.time = TimeNow();
  this.Animate = this.dragControl ? this.AnimateDrag : this.AnimatePosition;
  this.animTiming = (typeof TagCanvas[this.animTiming] == 'function' ?
    TagCanvas[this.animTiming] : TagCanvas.Smooth);
  if(this.shadowBlur || this.shadowOffset[0] || this.shadowOffset[1]) {
    // let the browser translate "red" into "#ff0000"
    this.ctxt.shadowColor = this.shadow;
    this.shadow = this.ctxt.shadowColor;
    this.shadowAlpha = ShadowAlphaBroken();
  } else {
    delete this.shadow;
  }
  this.Load();
  if(lctr && this.hideTags) {
    (function(t) {
    if(TagCanvas.loaded)
      t.HideTags();
    else
      AddHandler('load', function() { t.HideTags(); }, window);
    })(this);
  }

  this.yaw = this.initial ? this.initial[0] * this.maxSpeed : 0;
  this.pitch = this.initial ? this.initial[1] * this.maxSpeed : 0;
  if(this.tooltip) {
    if(this.tooltip == 'native') {
      this.Tooltip = this.TooltipNative;
    } else {
      this.Tooltip = this.TooltipDiv;
      if(!this.ttdiv) {
        this.ttdiv = doc.createElement('div');
        this.ttdiv.className = this.tooltipClass;
        this.ttdiv.style.position = 'absolute';
        this.ttdiv.style.zIndex = c.style.zIndex + 1;
        AddHandler('mouseover',function(e){e.target.style.display='none';},this.ttdiv);
        doc.body.appendChild(this.ttdiv);
      }
    }
  } else {
    this.Tooltip = this.TooltipNone;
  }
  if(!this.noMouse && !handlers[cid]) {
    AddHandler('mousemove', MouseMove, c);
    AddHandler('mouseout', MouseOut, c);
    AddHandler('mouseup', MouseUp, c);
    AddHandler('touchstart', TouchDown, c);
    AddHandler('touchend', TouchUp, c);
    AddHandler('touchcancel', TouchUp, c);
    AddHandler('touchmove', TouchMove, c);
    if(this.dragControl) {
      AddHandler('mousedown', MouseDown, c);
      AddHandler('selectstart', Nop, c);
    }
    if(this.wheelZoom) {
      AddHandler('mousewheel', MouseWheel, c);
      AddHandler('DOMMouseScroll', MouseWheel, c);
    }
    handlers[cid] = 1;
  }
  TagCanvas.started || (TagCanvas.started = setTimeout(DrawCanvas, this.interval));
}
TCproto = TagCanvas.prototype;
TCproto.SourceElements = function() {
  if(doc.querySelectorAll)
    return doc.querySelectorAll('#' + this.source);
  return [doc.getElementById(this.source)];
};
TCproto.HideTags = function() {
  var el = this.SourceElements(), i;
  for(i = 0; i < el.length; ++i)
    el[i].style.display = 'none';
};
TCproto.GetTags = function() {
  var el = this.SourceElements(), etl, tl = [], i, j;
  for(i = 0; i < el.length; ++i) {
    etl = el[i].getElementsByTagName('a');
    for(j = 0; j < etl.length; ++j) {
      tl.push(etl[j]);
    }
  }
  return tl;
};
TCproto.CreateTag = function(e, p) {
  var im = e.getElementsByTagName('img'), i, t, ts, font;
  p = p || [0, 0, 0];
  if(im.length) {
    i = new Image;
    i.src = im[0].src;
    t = new Tag(this, i, e, p, 0, 0);
    AddImage(i, im[0], t, this);
    return t;
  }
  ts = new TextSplitter(e);
  t = ts.Lines();
  font = this.textFont || FixFont(GetProperty(e,'font-family'));
  if(this.splitWidth)
    t = ts.SplitWidth(this.splitWidth, this.ctxt, font, this.textHeight);

  return new Tag(this, t, e, p, 2, this.textHeight + 2,
    this.textColour || GetProperty(e,'color'), font, ts.original);
};
TCproto.UpdateTag = function(t, a) {
  var colour = this.textColour || GetProperty(a, 'color'),
    font = this.textFont || FixFont(GetProperty(a, 'font-family'));
  t.title = a.title;
  if(t.colour != colour || t.textFont != font)
    t.SetFont(font, colour);
};
TCproto.Weight = function(tl) {
  var l = tl.length, w, i, weights = [];
  for(i = 0; i < l; ++i) {
    w = FindWeight(this, tl[i].a);
    if(w > this.max_weight) this.max_weight = w;
    if(w < this.min_weight) this.min_weight = w;
    weights.push(w);
  }
  if(this.max_weight > this.min_weight) {
    for(i = 0; i < l; ++i) {
      tl[i].SetWeight(weights[i]);
    }
  }
};
TCproto.Load = function() {
  var tl = this.GetTags(), taglist = [], shape,
    shapeArgs, rx, ry, rz, vl, i, tagmap = [], pfuncs = {
      sphere: PointsOnSphere,
      vcylinder: PointsOnCylinderV,
      hcylinder: PointsOnCylinderH,
      vring: PointsOnRingV,
      hring: PointsOnRingH
    };

  if(tl.length) {
    tagmap.length = tl.length;
    for(i = 0; i < tl.length; ++i)
      tagmap[i] = i;
    this.shuffleTags && Shuffle(tagmap);
    rx = 100 * this.radiusX;
    ry = 100 * this.radiusY;
    rz = 100 * this.radiusZ;
    this.max_radius = max(rx, max(ry, rz));

    if(this.shapeArgs) {
      this.shapeArgs[0] = tl.length;
    } else {
      shapeArgs = this.shape.toString().split(/[(),]/);
      shape = shapeArgs.shift();
      this.shape = pfuncs[shape] || pfuncs.sphere;
      this.shapeArgs = [tl.length, rx, ry, rz].concat(shapeArgs);
    }
    vl = this.shape.apply(this, this.shapeArgs);
    this.listLength = tl.length;
    for(i = 0; i < tl.length; ++i) {
      taglist.push(this.CreateTag(tl[tagmap[i]], vl[i]));
    }
    this.weight && this.Weight(taglist, true);
  }
  this.taglist = taglist;
};
TCproto.Update = function() {
  var tl = this.GetTags(), newlist = [],
    taglist = this.taglist, found,
    added = [], removed = [], vl, ol, nl, i, j;

  if(!this.shapeArgs)
    return this.Load();

  if(tl.length) {
    nl = this.listLength = tl.length;
    ol = taglist.length;

    // copy existing list, populate "removed"
    for(i = 0; i < ol; ++i) {
      newlist.push(taglist[i]);
      removed.push(i);
    }

    // find added and removed tags
    for(i = 0; i < nl; ++i) {
      for(j = 0, found = 0; j < ol; ++j) {
        if(taglist[j].EqualTo(tl[i])) {
          this.UpdateTag(newlist[j], tl[i]);
          found = removed[j] = -1;
        }
      }
      if(!found)
        added.push(i);
    }

    // clean out found tags from removed list
    for(i = 0, j = 0; i < ol; ++i) {
      if(removed[j] == -1)
        removed.splice(j,1);
      else
        ++j;
    }

    // insert new tags in gaps where old tags removed
    if(removed.length) {
      Shuffle(removed);
      while(removed.length && added.length) {
        i = removed.shift();
        j = added.shift();
        newlist[i] = this.CreateTag(tl[j]);
      }

      // remove any more (in reverse order)
      removed.sort(function(a,b) {return a-b});
      while(removed.length) {
        newlist.splice(removed.pop(), 1);
      }
    }

    // add any extra tags
    j = newlist.length / (added.length + 1);
    i = 0;
    while(added.length) {
      newlist.splice(ceil(++i * j), 0, this.CreateTag(tl[added.shift()]));
    }

    // assign correct positions to tags
    this.shapeArgs[0] = nl = newlist.length;
    vl = this.shape.apply(this, this.shapeArgs);
    for(i = 0; i < nl; ++i)
      newlist[i].position = new Vector(vl[i][0], vl[i][1], vl[i][2]);

    // reweight tags
    this.weight && this.Weight(newlist);
  }
  this.taglist = newlist;
};
TCproto.SetShadow = function(c) {
  c.shadowBlur = this.shadowBlur;
  c.shadowOffsetX = this.shadowOffset[0];
  c.shadowOffsetY = this.shadowOffset[1];
};
TCproto.Draw = function(t) {
  if(this.paused)
    return;
  var cv = this.canvas, cw = cv.width, ch = cv.height, max_sc = 0,
    tdelta = (t - this.time) * this.interval / 1000,
    x = cw / 2 + this.offsetX, y = ch / 2 + this.offsetY, c = this.ctxt,
    active, a, i, aindex = -1, tl = this.taglist, l = tl.length,
    frontsel = this.frontSelect, centreDrawn = (this.centreFunc == Nop), fixed;
  this.time = t;
  if(this.frozen && this.drawn)
    return this.Animate(cw,ch,tdelta);
  fixed = this.AnimateFixed();
  c.setTransform(1,0,0,1,0,0);
  this.active = null;
  for(i = 0; i < l; ++i)
    tl[i].Calc(this.transform, this.fixedAlpha);
  tl = SortList(tl, function(a,b) {return b.z-a.z});
  
  for(i = 0; i < l; ++i) {
    a = this.mx >= 0 && this.my >= 0 && this.taglist[i].CheckActive(c, x, y);
    if(a && a.sc > max_sc && (!frontsel || a.z <= 0)) {
      active = a;
      aindex = i;
      active.tag = this.taglist[i];
      max_sc = a.sc;
    }
  }
  this.active = active;

  this.txtOpt || (this.shadow && this.SetShadow(c));
  c.clearRect(0,0,cw,ch);
  for(i = 0; i < l; ++i) {
    if(!centreDrawn && tl[i].z <= 0) {
      // run the centreFunc if the next tag is at the front
      try { this.centreFunc(c, cw, ch, x, y); }
      catch(e) {
        alert(e);
        // don't run it again
        this.centreFunc = Nop;
      }
      centreDrawn = true;
    }

    if(!(active && active.tag == tl[i] && active.PreDraw(c, tl[i], x, y)))
      tl[i].Draw(c, x, y);
    active && active.tag == tl[i] && active.PostDraw(c);
  }
  if(this.freezeActive && active) {
    this.Freeze();
  } else {
    this.UnFreeze();
    this.drawn = (l == this.listLength);
  }
  if(this.fixedCallback) {
    this.fixedCallback(this,this.fixedCallbackTag);
    this.fixedCallback = null;
  }
  fixed || this.Animate(cw, ch, tdelta);
  active && active.LastDraw(c);
  cv.style.cursor = active ? this.activeCursor : '';
  this.Tooltip(active,this.taglist[aindex]);
};
TCproto.TooltipNone = function() { };
TCproto.TooltipNative = function(active,tag) {
  this.canvas.title = active && tag.title ? tag.title : '';
};
TCproto.TooltipDiv = function(active,tag) {
  var tc = this, s = tc.ttdiv.style, cid = tc.canvas.id, none = 'none';
  if(active && tag.title) {
    if(tag.title != tc.ttdiv.innerHTML)
      s.display = none;
    tc.ttdiv.innerHTML = tag.title;
    tag.title = tc.ttdiv.innerHTML;
    if(s.display == none && ! tc.tttimer) {
      tc.tttimer = setTimeout(function() {
        var p = AbsPos(cid);
        s.display = 'block';
        s.left = p.x + tc.mx + 'px';
        s.top = p.y + tc.my + 24 + 'px';
        tc.tttimer = null;
      }, tc.tooltipDelay);
    }
  } else {
    s.display = none;
  }
};
TCproto.Transform = function(tc, p, y) {
  if(p || y) {
    var sp = sin(p), cp = cos(p), sy = sin(y), cy = cos(y),
      ym = new Matrix([cy,0,sy, 0,1,0, -sy,0,cy]),
      pm = new Matrix([1,0,0, 0,cp,-sp, 0,sp,cp]);
    tc.transform = tc.transform.mul(ym.mul(pm));
  }
};
TCproto.AnimateFixed = function() {
  var fa, t1, angle, m, d;
  if(this.fadeIn) {
    t1 = TimeNow() - this.startTime;
    if(t1 >= this.fadeIn) {
      this.fadeIn = 0;
      this.fixedAlpha = 1;
    } else {
      this.fixedAlpha = t1 / this.fadeIn;
    }
  }
  if(this.fixedAnim) {
    if(!this.fixedAnim.transform)
      this.fixedAnim.transform = this.transform;
    fa = this.fixedAnim, t1 = TimeNow() - fa.t0, angle = fa.angle,
      m, d = this.animTiming(fa.t, t1);
    this.transform = fa.transform;
    if(t1 >= fa.t) {
      this.fixedCallbackTag = fa.tag;
      this.fixedCallback = fa.cb;
      this.fixedAnim = this.yaw = this.pitch = 0;
    } else {
      angle *= d;
    }
    m = Matrix.Rotation(angle, fa.axis);
    this.transform = this.transform.mul(m);
    return (this.fixedAnim != 0);
  }
  return false;
};
TCproto.AnimatePosition = function(w, h, t) {
  var tc = this, x = tc.mx, y = tc.my, s, r;
  if(!tc.frozen && x >= 0 && y >= 0 && x < w && y < h) {
    s = tc.maxSpeed, r = tc.reverse ? -1 : 1;
    tc.lx || (tc.yaw = r * t * ((s * 2 * x / w) - s));
    tc.ly || (tc.pitch = r * t * -((s * 2 * y / h) - s));
    tc.initial = null;
  } else if(!tc.initial) {
    if(tc.frozen && !tc.freezeDecel)
      tc.yaw = tc.pitch = 0;
    else
      tc.Decel(tc);
  }
  this.Transform(tc, tc.pitch, tc.yaw);
};
TCproto.AnimateDrag = function(w, h, t) {
  var tc = this, rs = 100 * t * tc.maxSpeed / tc.max_radius / tc.zoom;
  if(tc.dx || tc.dy) {
    tc.lx || (tc.yaw = tc.dx * rs / tc.stretchX);
    tc.ly || (tc.pitch = tc.dy * -rs / tc.stretchY);
    tc.dx = tc.dy = 0;
    tc.initial = null;
  } else if(!tc.initial) {
    tc.Decel(tc);
  }
  this.Transform(tc, tc.pitch, tc.yaw);
};
TCproto.Freeze = function() {
  if(!this.frozen) {
    this.preFreeze = [this.yaw, this.pitch];
    this.frozen = 1;
    this.drawn = 0;
  }
};
TCproto.UnFreeze = function() {
  if(this.frozen) {
    this.yaw = this.preFreeze[0];
    this.pitch = this.preFreeze[1];
    this.frozen = 0;
  }
};
TCproto.Decel = function(tc) {
  var s = tc.minSpeed, ay = abs(tc.yaw), ap = abs(tc.pitch);
  if(!tc.lx && ay > s)
    tc.yaw = ay > tc.z0 ? tc.yaw * tc.decel : 0;
  if(!tc.ly && ap > s)
    tc.pitch = ap > tc.z0 ? tc.pitch * tc.decel : 0;
};
TCproto.Zoom = function(r) {
  this.z2 = this.z1 * (1/r);
  this.drawn = 0;
};
TCproto.Clicked = function(e) {
  var a = this.active;
  try {
    if(a && a.tag)
      if(this.clickToFront === false || this.clickToFront === null)
        a.tag.Clicked(e);
      else
        this.TagToFront(a.tag, this.clickToFront, function() { a.tag.Clicked(e); });
  } catch(ex) {
  }
};
TCproto.Wheel = function(i) {
  var z = this.zoom + this.zoomStep * (i ? 1 : -1);
  this.zoom = min(this.zoomMax,max(this.zoomMin,z));
  this.Zoom(this.zoom);
};
TCproto.BeginDrag = function(e) {
  this.down = EventXY(e, this.canvas);
  e.cancelBubble = true;
  e.returnValue = false;
  e.preventDefault && e.preventDefault();
};
TCproto.Drag = function(e, p) {
  if(this.dragControl && this.down) {
    var t2 = this.dragThreshold * this.dragThreshold,
      dx = p.x - this.down.x, dy = p.y - this.down.y;
    if(this.dragging || dx * dx + dy * dy > t2) {
      this.dx = dx;
      this.dy = dy;
      this.dragging = 1;
      this.down = p;
    }
  }
};
TCproto.EndDrag = function() {
  var res = this.dragging;
  this.dragging = this.down = null;
  return res;
};
TCproto.Pause = function() { this.paused = true; };
TCproto.Resume = function() { this.paused = false; };
TCproto.FindTag = function(t) {
  if(!Defined(t))
    return null;
  Defined(t.index) && (t = t.index);
  if(!IsObject(t))
    return this.taglist[t];
  var srch, tgt, i;
  if(Defined(t.id))
    srch = 'id', tgt = t.id;
  else if(Defined(t.text))
    srch = 'innerText', tgt = t.text;

  for(i = 0; i < this.taglist.length; ++i)
    if(this.taglist[i].a[srch] == tgt)
      return this.taglist[i];
};
TCproto.RotateTag = function(tag, lt, lg, time, callback) {
  var t = tag.xformed, v1 = new Vector(t.x, t.y, t.z),
    v2 = MakeVector(lg, lt), angle = v1.angle(v2), u = v1.cross(v2).unit();
  if(angle == 0) {
    this.fixedCallbackTag = tag;
    this.fixedCallback = callback;
  } else {
    this.fixedAnim = {angle: -angle, axis: u, t: time, t0: TimeNow(), cb: callback, tag: tag};
  }
};
TCproto.TagToFront = function(tag, time, callback) {
  this.RotateTag(tag, 0, 0, time, callback);
};
TagCanvas.Start = function(id,l,o) {
  TagCanvas.tc[id] = new TagCanvas(id,l,o);
};
function tccall(f,id) {
  TagCanvas.tc[id] && TagCanvas.tc[id][f]();
}
TagCanvas.Linear = function(t, t0) { return t0 / t; }
TagCanvas.Smooth = function(t, t0) { return 0.5 - cos(t0 * Math.PI / t) / 2; }
TagCanvas.Pause = function(id) { tccall('Pause',id); };
TagCanvas.Resume = function(id) { tccall('Resume',id); };
TagCanvas.Reload = function(id) { tccall('Load',id); };
TagCanvas.Update = function(id) { tccall('Update',id); };
TagCanvas.TagToFront = function(id, options) {
  if(!IsObject(options))
    return false;
  options.lat = options.lng = 0;
  return TagCanvas.RotateTag(id, options);
};
TagCanvas.RotateTag = function(id, options) {
  if(!IsObject(options))
    return false;
  if(TagCanvas.tc[id]) {
    if(isNaN(options['time']))
      options.time = 500;
    var tt = TagCanvas.tc[id].FindTag(options);
    if(tt) {
      TagCanvas.tc[id].RotateTag(tt, options['lat'], options['lng'], 
        options['time'], options['callback']);
      return true;
    }
  }
  return false;
};
TagCanvas.NextFrame = function(iv) {
  var raf = window.requestAnimationFrame = window.requestAnimationFrame ||
    window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame ||
    window.msRequestAnimationFrame;
  TagCanvas.NextFrame = raf ? TagCanvas.NextFrameRAF : TagCanvas.NextFrameTimeout;
  TagCanvas.NextFrame(iv);
};
TagCanvas.NextFrameRAF = function() {
  requestAnimationFrame(DrawCanvas);
};
TagCanvas.NextFrameTimeout = function(iv) {
  setTimeout(DrawCanvas, iv);
};
TagCanvas.tc = {};
TagCanvas.options = {
z1: 20000,
z2: 20000,
z0: 0.0002,
freezeActive: false,
freezeDecel: false,
activeCursor: 'pointer',
pulsateTo: 1,
pulsateTime: 3,
reverse: false,
depth: 0.5,
maxSpeed: 0.05,
minSpeed: 0,
decel: 0.95,
interval: 20,
minBrightness: 0.1,
maxBrightness: 1,
outlineColour: '#ffff99',
outlineThickness: 2,
outlineOffset: 5,
outlineMethod: 'outline',
textColour: '#ff99ff',
textHeight: 15,
textFont: 'Helvetica, Arial, sans-serif',
shadow: '#000',
shadowBlur: 0,
shadowOffset: [0,0],
initial: null,
hideTags: true,
zoom: 1,
weight: false,
weightMode: 'size',
weightFrom: null,
weightSize: 1,
weightSizeMin: null,
weightSizeMax: null,
weightGradient: {0:'#f00', 0.33:'#ff0', 0.66:'#0f0', 1:'#00f'},
txtOpt: true,
txtScale: 2,
frontSelect: false,
wheelZoom: true,
zoomMin: 0.3,
zoomMax: 3,
zoomStep: 0.05,
shape: 'sphere',
lock: null,
tooltip: null,
tooltipDelay: 300,
tooltipClass: 'tctooltip',
radiusX: 1,
radiusY: 1,
radiusZ: 1,
stretchX: 1,
stretchY: 1,
offsetX: 0,
offsetY: 0,
shuffleTags: false,
noSelect: false,
noMouse: false,
imageScale: 1,
paused: false,
dragControl: false,
dragThreshold: 4,
centreFunc: Nop,
splitWidth: 0,
animTiming: 'Smooth',
clickToFront: false,
fadeIn: 0
};
for(i in TagCanvas.options) TagCanvas[i] = TagCanvas.options[i];
window.TagCanvas = TagCanvas;
jQuery.fn.tagcanvas = function(options, lctr) {
  var fn = {
    pause: function() {
      $(this).each(function() { tccall('Pause',$(this)[0].id); });
    },
    resume: function() {
      $(this).each(function() { tccall('Resume',$(this)[0].id); });
    },
    reload: function() {
      $(this).each(function() { tccall('Load',$(this)[0].id); });
    },
    update: function() {
      $(this).each(function() { tccall('Update',$(this)[0].id); });
    },
    tagtofront: function() {
      $(this).each(function() { TagCanvas.TagToFront($(this)[0].id, lctr); });
    },
    rotatetag: function() {
      $(this).each(function() { TagCanvas.RotateTag($(this)[0].id, lctr); });
    }
  };
  if(typeof options == 'string' && fn[options]) {
    fn[options].apply(this);
  } else {
    TagCanvas.jquery = 1;
    $(this).each(function() { TagCanvas.Start($(this)[0].id, lctr, options); });
    return TagCanvas.started;
  }
};

// set a flag for when the window has loaded
AddHandler('load',function(){TagCanvas.loaded=1},window);
})(jQuery);

/*!
 * jQuery blockUI plugin
 * Version 2.66.0-2013.10.09
 * Requires jQuery v1.7 or later
 *
 * Examples at: http://malsup.com/jquery/block/
 * Copyright (c) 2007-2013 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * Thanks to Amir-Hossein Sobhi for some excellent contributions!
 */

;(function() {
/*jshint eqeqeq:false curly:false latedef:false */
"use strict";

	function setup($) {
		$.fn._fadeIn = $.fn.fadeIn;

		var noOp = $.noop || function() {};

		// this bit is to ensure we don't call setExpression when we shouldn't (with extra muscle to handle
		// confusing userAgent strings on Vista)
		var msie = /MSIE/.test(navigator.userAgent);
		var ie6  = /MSIE 6.0/.test(navigator.userAgent) && ! /MSIE 8.0/.test(navigator.userAgent);
		var mode = document.documentMode || 0;
		var setExpr = $.isFunction( document.createElement('div').style.setExpression );

		// global $ methods for blocking/unblocking the entire page
		$.blockUI   = function(opts) { install(window, opts); };
		$.unblockUI = function(opts) { remove(window, opts); };

		// convenience method for quick growl-like notifications  (http://www.google.com/search?q=growl)
		$.growlUI = function(title, message, timeout, onClose) {
			var $m = $('<div class="growlUI"></div>');
			if (title) $m.append('<h1>'+title+'</h1>');
			if (message) $m.append('<h2>'+message+'</h2>');
			if (timeout === undefined) timeout = 3000;

			// Added by konapun: Set timeout to 30 seconds if this growl is moused over, like normal toast notifications
			var callBlock = function(opts) {
				opts = opts || {};

				$.blockUI({
					message: $m,
					fadeIn : typeof opts.fadeIn  !== 'undefined' ? opts.fadeIn  : 700,
					fadeOut: typeof opts.fadeOut !== 'undefined' ? opts.fadeOut : 1000,
					timeout: typeof opts.timeout !== 'undefined' ? opts.timeout : timeout,
					centerY: false,
					showOverlay: false,
					onUnblock: onClose,
					css: $.blockUI.defaults.growlCSS
				});
			};

			callBlock();
			var nonmousedOpacity = $m.css('opacity');
			$m.mouseover(function() {
				callBlock({
					fadeIn: 0,
					timeout: 30000
				});

				var displayBlock = $('.blockMsg');
				displayBlock.stop(); // cancel fadeout if it has started
				displayBlock.fadeTo(300, 1); // make it easier to read the message by removing transparency
			}).mouseout(function() {
				$('.blockMsg').fadeOut(1000);
			});
			// End konapun additions
		};

		// plugin method for blocking element content
		$.fn.block = function(opts) {
			if ( this[0] === window ) {
				$.blockUI( opts );
				return this;
			}
			var fullOpts = $.extend({}, $.blockUI.defaults, opts || {});
			this.each(function() {
				var $el = $(this);
				if (fullOpts.ignoreIfBlocked && $el.data('blockUI.isBlocked'))
					return;
				$el.unblock({ fadeOut: 0 });
			});

			return this.each(function() {
				if ($.css(this,'position') == 'static') {
					this.style.position = 'relative';
					$(this).data('blockUI.static', true);
				}
				this.style.zoom = 1; // force 'hasLayout' in ie
				install(this, opts);
			});
		};

		// plugin method for unblocking element content
		$.fn.unblock = function(opts) {
			if ( this[0] === window ) {
				$.unblockUI( opts );
				return this;
			}
			return this.each(function() {
				remove(this, opts);
			});
		};

		$.blockUI.version = 2.66; // 2nd generation blocking at no extra cost!

		// override these in your code to change the default behavior and style
		$.blockUI.defaults = {
			// message displayed when blocking (use null for no message)
			message:  '<h1>Please wait...</h1>',

			title: null,		// title string; only used when theme == true
			draggable: true,	// only used when theme == true (requires jquery-ui.js to be loaded)

			theme: false, // set to true to use with jQuery UI themes

			// styles for the message when blocking; if you wish to disable
			// these and use an external stylesheet then do this in your code:
			// $.blockUI.defaults.css = {};
			css: {
				padding:	0,
				margin:		0,
				width:		'30%',
				top:		'40%',
				left:		'35%',
				textAlign:	'center',
				color:		'#000',
				border:		'3px solid #aaa',
				backgroundColor:'#fff',
				cursor:		'wait'
			},

			// minimal style set used when themes are used
			themedCSS: {
				width:	'30%',
				top:	'40%',
				left:	'35%'
			},

			// styles for the overlay
			overlayCSS:  {
				backgroundColor:	'#000',
				opacity:			0.6,
				cursor:				'wait'
			},

			// style to replace wait cursor before unblocking to correct issue
			// of lingering wait cursor
			cursorReset: 'default',

			// styles applied when using $.growlUI
			growlCSS: {
				width:		'350px',
				top:		'10px',
				left:		'',
				right:		'10px',
				border:		'none',
				padding:	'5px',
				opacity:	0.6,
				cursor:		'default',
				color:		'#fff',
				backgroundColor: '#000',
				'-webkit-border-radius':'10px',
				'-moz-border-radius':	'10px',
				'border-radius':		'10px'
			},

			// IE issues: 'about:blank' fails on HTTPS and javascript:false is s-l-o-w
			// (hat tip to Jorge H. N. de Vasconcelos)
			/*jshint scripturl:true */
			iframeSrc: /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank',

			// force usage of iframe in non-IE browsers (handy for blocking applets)
			forceIframe: false,

			// z-index for the blocking overlay
			baseZ: 1000,

			// set these to true to have the message automatically centered
			centerX: true, // <-- only effects element blocking (page block controlled via css above)
			centerY: true,

			// allow body element to be stetched in ie6; this makes blocking look better
			// on "short" pages.  disable if you wish to prevent changes to the body height
			allowBodyStretch: true,

			// enable if you want key and mouse events to be disabled for content that is blocked
			bindEvents: true,

			// be default blockUI will supress tab navigation from leaving blocking content
			// (if bindEvents is true)
			constrainTabKey: true,

			// fadeIn time in millis; set to 0 to disable fadeIn on block
			fadeIn:  200,

			// fadeOut time in millis; set to 0 to disable fadeOut on unblock
			fadeOut:  400,

			// time in millis to wait before auto-unblocking; set to 0 to disable auto-unblock
			timeout: 0,

			// disable if you don't want to show the overlay
			showOverlay: true,

			// if true, focus will be placed in the first available input field when
			// page blocking
			focusInput: true,

            // elements that can receive focus
            focusableElements: ':input:enabled:visible',

			// suppresses the use of overlay styles on FF/Linux (due to performance issues with opacity)
			// no longer needed in 2012
			// applyPlatformOpacityRules: true,

			// callback method invoked when fadeIn has completed and blocking message is visible
			onBlock: null,

			// callback method invoked when unblocking has completed; the callback is
			// passed the element that has been unblocked (which is the window object for page
			// blocks) and the options that were passed to the unblock call:
			//	onUnblock(element, options)
			onUnblock: null,

			// callback method invoked when the overlay area is clicked.
			// setting this will turn the cursor to a pointer, otherwise cursor defined in overlayCss will be used.
			onOverlayClick: null,

			// don't ask; if you really must know: http://groups.google.com/group/jquery-en/browse_thread/thread/36640a8730503595/2f6a79a77a78e493#2f6a79a77a78e493
			quirksmodeOffsetHack: 4,

			// class name of the message block
			blockMsgClass: 'blockMsg',

			// if it is already blocked, then ignore it (don't unblock and reblock)
			ignoreIfBlocked: false
		};

		// private data and functions follow...

		var pageBlock = null;
		var pageBlockEls = [];

		function install(el, opts) {
			var css, themedCSS;
			var full = (el == window);
			var msg = (opts && opts.message !== undefined ? opts.message : undefined);
			opts = $.extend({}, $.blockUI.defaults, opts || {});

			if (opts.ignoreIfBlocked && $(el).data('blockUI.isBlocked'))
				return;

			opts.overlayCSS = $.extend({}, $.blockUI.defaults.overlayCSS, opts.overlayCSS || {});
			css = $.extend({}, $.blockUI.defaults.css, opts.css || {});
			if (opts.onOverlayClick)
				opts.overlayCSS.cursor = 'pointer';

			themedCSS = $.extend({}, $.blockUI.defaults.themedCSS, opts.themedCSS || {});
			msg = msg === undefined ? opts.message : msg;

			// remove the current block (if there is one)
			if (full && pageBlock)
				remove(window, {fadeOut:0});

			// if an existing element is being used as the blocking content then we capture
			// its current place in the DOM (and current display style) so we can restore
			// it when we unblock
			if (msg && typeof msg != 'string' && (msg.parentNode || msg.jquery)) {
				var node = msg.jquery ? msg[0] : msg;
				var data = {};
				$(el).data('blockUI.history', data);
				data.el = node;
				data.parent = node.parentNode;
				data.display = node.style.display;
				data.position = node.style.position;
				if (data.parent)
					data.parent.removeChild(node);
			}

			$(el).data('blockUI.onUnblock', opts.onUnblock);
			var z = opts.baseZ;

			// blockUI uses 3 layers for blocking, for simplicity they are all used on every platform;
			// layer1 is the iframe layer which is used to supress bleed through of underlying content
			// layer2 is the overlay layer which has opacity and a wait cursor (by default)
			// layer3 is the message content that is displayed while blocking
			var lyr1, lyr2, lyr3, s;
			if (msie || opts.forceIframe)
				lyr1 = $('<iframe class="blockUI" style="z-index:'+ (z++) +';display:none;border:none;margin:0;padding:0;position:absolute;width:100%;height:100%;top:0;left:0" src="'+opts.iframeSrc+'"></iframe>');
			else
				lyr1 = $('<div class="blockUI" style="display:none"></div>');

			if (opts.theme)
				lyr2 = $('<div class="blockUI blockOverlay ui-widget-overlay" style="z-index:'+ (z++) +';display:none"></div>');
			else
				lyr2 = $('<div class="blockUI blockOverlay" style="z-index:'+ (z++) +';display:none;border:none;margin:0;padding:0;width:100%;height:100%;top:0;left:0"></div>');

			if (opts.theme && full) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage ui-dialog ui-widget ui-corner-all" style="z-index:'+(z+10)+';display:none;position:fixed">';
				if ( opts.title ) {
					s += '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+(opts.title || '&nbsp;')+'</div>';
				}
				s += '<div class="ui-widget-content ui-dialog-content"></div>';
				s += '</div>';
			}
			else if (opts.theme) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockElement ui-dialog ui-widget ui-corner-all" style="z-index:'+(z+10)+';display:none;position:absolute">';
				if ( opts.title ) {
					s += '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+(opts.title || '&nbsp;')+'</div>';
				}
				s += '<div class="ui-widget-content ui-dialog-content"></div>';
				s += '</div>';
			}
			else if (full) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage" style="z-index:'+(z+10)+';display:none;position:fixed"></div>';
			}
			else {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockElement" style="z-index:'+(z+10)+';display:none;position:absolute"></div>';
			}
			lyr3 = $(s);

			// if we have a message, style it
			if (msg) {
				if (opts.theme) {
					lyr3.css(themedCSS);
					lyr3.addClass('ui-widget-content');
				}
				else
					lyr3.css(css);
			}

			// style the overlay
			if (!opts.theme /*&& (!opts.applyPlatformOpacityRules)*/)
				lyr2.css(opts.overlayCSS);
			lyr2.css('position', full ? 'fixed' : 'absolute');

			// make iframe layer transparent in IE
			if (msie || opts.forceIframe)
				lyr1.css('opacity',0.0);

			//$([lyr1[0],lyr2[0],lyr3[0]]).appendTo(full ? 'body' : el);
			var layers = [lyr1,lyr2,lyr3], $par = full ? $('body') : $(el);
			$.each(layers, function() {
				this.appendTo($par);
			});

			if (opts.theme && opts.draggable && $.fn.draggable) {
				lyr3.draggable({
					handle: '.ui-dialog-titlebar',
					cancel: 'li'
				});
			}

			// ie7 must use absolute positioning in quirks mode and to account for activex issues (when scrolling)
			var expr = setExpr && (!$.support.boxModel || $('object,embed', full ? null : el).length > 0);
			if (ie6 || expr) {
				// give body 100% height
				if (full && opts.allowBodyStretch && $.support.boxModel)
					$('html,body').css('height','100%');

				// fix ie6 issue when blocked element has a border width
				if ((ie6 || !$.support.boxModel) && !full) {
					var t = sz(el,'borderTopWidth'), l = sz(el,'borderLeftWidth');
					var fixT = t ? '(0 - '+t+')' : 0;
					var fixL = l ? '(0 - '+l+')' : 0;
				}

				// simulate fixed position
				$.each(layers, function(i,o) {
					var s = o[0].style;
					s.position = 'absolute';
					if (i < 2) {
						if (full)
							s.setExpression('height','Math.max(document.body.scrollHeight, document.body.offsetHeight) - (jQuery.support.boxModel?0:'+opts.quirksmodeOffsetHack+') + "px"');
						else
							s.setExpression('height','this.parentNode.offsetHeight + "px"');
						if (full)
							s.setExpression('width','jQuery.support.boxModel && document.documentElement.clientWidth || document.body.clientWidth + "px"');
						else
							s.setExpression('width','this.parentNode.offsetWidth + "px"');
						if (fixL) s.setExpression('left', fixL);
						if (fixT) s.setExpression('top', fixT);
					}
					else if (opts.centerY) {
						if (full) s.setExpression('top','(document.documentElement.clientHeight || document.body.clientHeight) / 2 - (this.offsetHeight / 2) + (blah = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "px"');
						s.marginTop = 0;
					}
					else if (!opts.centerY && full) {
						var top = (opts.css && opts.css.top) ? parseInt(opts.css.top, 10) : 0;
						var expression = '((document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + '+top+') + "px"';
						s.setExpression('top',expression);
					}
				});
			}

			// show the message
			if (msg) {
				if (opts.theme)
					lyr3.find('.ui-widget-content').append(msg);
				else
					lyr3.append(msg);
				if (msg.jquery || msg.nodeType)
					$(msg).show();
			}

			if ((msie || opts.forceIframe) && opts.showOverlay)
				lyr1.show(); // opacity is zero
			if (opts.fadeIn) {
				var cb = opts.onBlock ? opts.onBlock : noOp;
				var cb1 = (opts.showOverlay && !msg) ? cb : noOp;
				var cb2 = msg ? cb : noOp;
				if (opts.showOverlay)
					lyr2._fadeIn(opts.fadeIn, cb1);
				if (msg)
					lyr3._fadeIn(opts.fadeIn, cb2);
			}
			else {
				if (opts.showOverlay)
					lyr2.show();
				if (msg)
					lyr3.show();
				if (opts.onBlock)
					opts.onBlock();
			}

			// bind key and mouse events
			bind(1, el, opts);

			if (full) {
				pageBlock = lyr3[0];
				pageBlockEls = $(opts.focusableElements,pageBlock);
				if (opts.focusInput)
					setTimeout(focus, 20);
			}
			else
				center(lyr3[0], opts.centerX, opts.centerY);

			if (opts.timeout) {
				// auto-unblock
				var to = setTimeout(function() {
					if (full)
						$.unblockUI(opts);
					else
						$(el).unblock(opts);
				}, opts.timeout);
				$(el).data('blockUI.timeout', to);
			}
		}

		// remove the block
		function remove(el, opts) {
			var count;
			var full = (el == window);
			var $el = $(el);
			var data = $el.data('blockUI.history');
			var to = $el.data('blockUI.timeout');
			if (to) {
				clearTimeout(to);
				$el.removeData('blockUI.timeout');
			}
			opts = $.extend({}, $.blockUI.defaults, opts || {});
			bind(0, el, opts); // unbind events

			if (opts.onUnblock === null) {
				opts.onUnblock = $el.data('blockUI.onUnblock');
				$el.removeData('blockUI.onUnblock');
			}

			var els;
			if (full) // crazy selector to handle odd field errors in ie6/7
				els = $('body').children().filter('.blockUI').add('body > .blockUI');
			else
				els = $el.find('>.blockUI');

			// fix cursor issue
			if ( opts.cursorReset ) {
				if ( els.length > 1 )
					els[1].style.cursor = opts.cursorReset;
				if ( els.length > 2 )
					els[2].style.cursor = opts.cursorReset;
			}

			if (full)
				pageBlock = pageBlockEls = null;

			if (opts.fadeOut) {
				count = els.length;
				els.stop().fadeOut(opts.fadeOut, function() {
					if ( --count === 0)
						reset(els,data,opts,el);
				});
			}
			else
				reset(els, data, opts, el);
		}

		// move blocking element back into the DOM where it started
		function reset(els,data,opts,el) {
			var $el = $(el);
			if ( $el.data('blockUI.isBlocked') )
				return;

			els.each(function(i,o) {
				// remove via DOM calls so we don't lose event handlers
				if (this.parentNode)
					this.parentNode.removeChild(this);
			});

			if (data && data.el) {
				data.el.style.display = data.display;
				data.el.style.position = data.position;
				if (data.parent)
					data.parent.appendChild(data.el);
				$el.removeData('blockUI.history');
			}

			if ($el.data('blockUI.static')) {
				$el.css('position', 'static'); // #22
			}

			if (typeof opts.onUnblock == 'function')
				opts.onUnblock(el,opts);

			// fix issue in Safari 6 where block artifacts remain until reflow
			var body = $(document.body), w = body.width(), cssW = body[0].style.width;
			body.width(w-1).width(w);
			body[0].style.width = cssW;
		}

		// bind/unbind the handler
		function bind(b, el, opts) {
			var full = el == window, $el = $(el);

			// don't bother unbinding if there is nothing to unbind
			if (!b && (full && !pageBlock || !full && !$el.data('blockUI.isBlocked')))
				return;

			$el.data('blockUI.isBlocked', b);

			// don't bind events when overlay is not in use or if bindEvents is false
			if (!full || !opts.bindEvents || (b && !opts.showOverlay))
				return;

			// bind anchors and inputs for mouse and key events
			var events = 'mousedown mouseup keydown keypress keyup touchstart touchend touchmove';
			if (b)
				$(document).bind(events, opts, handler);
			else
				$(document).unbind(events, handler);

		// former impl...
		//		var $e = $('a,:input');
		//		b ? $e.bind(events, opts, handler) : $e.unbind(events, handler);
		}

		// event handler to suppress keyboard/mouse events when blocking
		function handler(e) {
			// allow tab navigation (conditionally)
			if (e.type === 'keydown' && e.keyCode && e.keyCode == 9) {
				if (pageBlock && e.data.constrainTabKey) {
					var els = pageBlockEls;
					var fwd = !e.shiftKey && e.target === els[els.length-1];
					var back = e.shiftKey && e.target === els[0];
					if (fwd || back) {
						setTimeout(function(){focus(back);},10);
						return false;
					}
				}
			}
			var opts = e.data;
			var target = $(e.target);
			if (target.hasClass('blockOverlay') && opts.onOverlayClick)
				opts.onOverlayClick(e);

			// allow events within the message content
			if (target.parents('div.' + opts.blockMsgClass).length > 0)
				return true;

			// allow events for content that is not being blocked
			return target.parents().children().filter('div.blockUI').length === 0;
		}

		function focus(back) {
			if (!pageBlockEls)
				return;
			var e = pageBlockEls[back===true ? pageBlockEls.length-1 : 0];
			if (e)
				e.focus();
		}

		function center(el, x, y) {
			var p = el.parentNode, s = el.style;
			var l = ((p.offsetWidth - el.offsetWidth)/2) - sz(p,'borderLeftWidth');
			var t = ((p.offsetHeight - el.offsetHeight)/2) - sz(p,'borderTopWidth');
			if (x) s.left = l > 0 ? (l+'px') : '0';
			if (y) s.top  = t > 0 ? (t+'px') : '0';
		}

		function sz(el, p) {
			return parseInt($.css(el,p),10)||0;
		}

	}


	/*global define:true */
	if (typeof define === 'function' && define.amd && define.amd.jQuery) {
		define(['jquery'], setup);
	} else {
		setup(jQuery);
	}

})();


