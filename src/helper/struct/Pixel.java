package helper.struct;

import helper.enums.Color;

public class Pixel {
    public byte red;
    public byte green;
    public byte blue;
    public byte alpha;

    public Pixel(){
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.alpha = (byte)255;
    }

    public Pixel(float red,float green,float blue,float alpha){
        this.red = (byte)(red * 255.0f);
        this.green = (byte)(green * 255.0f);
        this.blue = (byte)(blue * 255.0f);
        this.alpha = (byte)(alpha * 255.0f);
    }

    public Pixel(int red,int green,int blue,int alpha){
        this.red = (byte)(red);
        this.green = (byte)(green);
        this.blue = (byte)(blue);
        this.alpha = (byte)(alpha);
    }

    public Pixel(byte red,byte green,byte blue,byte alpha){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Pixel(int color){
        this.alpha = (byte)(color);
        this.blue = (byte)(color >> 8);
        this.green = (byte)(color >> 16);
        this.red = (byte)(color >> 24);
    }

    public boolean pixelEquals(Color color){
        int clr = color.getValue();
        byte alpha = (byte)(clr);
        byte blue = (byte)(clr >> 8);
        byte green = (byte)(clr >> 16);
        byte red = (byte)(clr >> 24);
        return alpha == this.alpha && blue == this.blue && green == this.green && red == this.red;
    }

    public static Pixel mult(Pixel px,float value){
        float fr = Math.min(255.0f,Math.max(0.0f,((float)px.red)*value));
        float fg = Math.min(255.0f,Math.max(0.0f,((float)px.green)*value));
        float fb = Math.min(255.0f,Math.max(0.0f,((float)px.blue)*value));
        return new Pixel((byte)fr,(byte)fg,(byte)fb,px.alpha);
    }

    public static Pixel add(Pixel a,Pixel b){
        int nr = Math.min(255,Math.max(0,(int)(a.red) + (int)b.red));
        int ng = Math.min(255,Math.max(0,(int)(a.green) + (int)b.green));
        int nb = Math.min(255,Math.max(0,(int)(a.blue) + (int)b.blue));
        return new Pixel(nr,ng,nb,a.alpha);
    }

    @Override
    public String toString(){
        return "[ Red: %d ] [ Green: %d ] [ Blue: %d ] [ Alpha: %d ]".formatted(red,green,blue,alpha);
    }
}

/*
* Pixel::Pixel()
	{ r = 0; g = 0; b = 0; a = nDefaultAlpha; }

	Pixel::Pixel(uint8_t red, uint8_t green, uint8_t blue, uint8_t alpha)
	{ n = red | (green << 8) | (blue << 16) | (alpha << 24); } // Thanks jarekpelczar

	Pixel::Pixel(uint32_t p)
	{ n = p; }

	bool Pixel::operator==(const Pixel& p) const
	{ return n == p.n; }

	bool Pixel::operator!=(const Pixel& p) const
	{ return n != p.n; }

	Pixel  Pixel::operator * (const float i) const
	{
		float fR = std::min(255.0f, std::max(0.0f, float(r) * i));
		float fG = std::min(255.0f, std::max(0.0f, float(g) * i));
		float fB = std::min(255.0f, std::max(0.0f, float(b) * i));
		return Pixel(uint8_t(fR), uint8_t(fG), uint8_t(fB), a);
	}

	Pixel  Pixel::operator / (const float i) const
	{
		float fR = std::min(255.0f, std::max(0.0f, float(r) / i));
		float fG = std::min(255.0f, std::max(0.0f, float(g) / i));
		float fB = std::min(255.0f, std::max(0.0f, float(b) / i));
		return Pixel(uint8_t(fR), uint8_t(fG), uint8_t(fB), a);
	}

	Pixel& Pixel::operator *=(const float i)
	{
		this->r = uint8_t(std::min(255.0f, std::max(0.0f, float(r) * i)));
		this->g = uint8_t(std::min(255.0f, std::max(0.0f, float(g) * i)));
		this->b = uint8_t(std::min(255.0f, std::max(0.0f, float(b) * i)));
		return *this;
	}

	Pixel& Pixel::operator /=(const float i)
	{
		this->r = uint8_t(std::min(255.0f, std::max(0.0f, float(r) / i)));
		this->g = uint8_t(std::min(255.0f, std::max(0.0f, float(g) / i)));
		this->b = uint8_t(std::min(255.0f, std::max(0.0f, float(b) / i)));
		return *this;
	}

	Pixel  Pixel::operator + (const Pixel& p) const
	{
		uint8_t nR = uint8_t(std::min(255, std::max(0, int(r) + int(p.r))));
		uint8_t nG = uint8_t(std::min(255, std::max(0, int(g) + int(p.g))));
		uint8_t nB = uint8_t(std::min(255, std::max(0, int(b) + int(p.b))));
		return Pixel(nR, nG, nB, a);
	}

	Pixel  Pixel::operator - (const Pixel& p) const
	{
		uint8_t nR = uint8_t(std::min(255, std::max(0, int(r) - int(p.r))));
		uint8_t nG = uint8_t(std::min(255, std::max(0, int(g) - int(p.g))));
		uint8_t nB = uint8_t(std::min(255, std::max(0, int(b) - int(p.b))));
		return Pixel(nR, nG, nB, a);
	}

	Pixel& Pixel::operator += (const Pixel& p)
	{
		this->r = uint8_t(std::min(255, std::max(0, int(r) + int(p.r))));
		this->g = uint8_t(std::min(255, std::max(0, int(g) + int(p.g))));
		this->b = uint8_t(std::min(255, std::max(0, int(b) + int(p.b))));
		return *this;
	}

	Pixel& Pixel::operator -= (const Pixel& p) // Thanks Au Lit
	{
		this->r = uint8_t(std::min(255, std::max(0, int(r) - int(p.r))));
		this->g = uint8_t(std::min(255, std::max(0, int(g) - int(p.g))));
		this->b = uint8_t(std::min(255, std::max(0, int(b) - int(p.b))));
		return *this;
	}

	Pixel Pixel::operator * (const Pixel& p) const
	{
		uint8_t nR = uint8_t(std::min(255.0f, std::max(0.0f, float(r) * float(p.r) / 255.0f)));
		uint8_t nG = uint8_t(std::min(255.0f, std::max(0.0f, float(g) * float(p.g) / 255.0f)));
		uint8_t nB = uint8_t(std::min(255.0f, std::max(0.0f, float(b) * float(p.b) / 255.0f)));
		uint8_t nA = uint8_t(std::min(255.0f, std::max(0.0f, float(a) * float(p.a) / 255.0f)));
		return Pixel(nR, nG, nB, nA);
	}

	Pixel& Pixel::operator *=(const Pixel& p)
	{
		this->r = uint8_t(std::min(255.0f, std::max(0.0f, float(r) * float(p.r) / 255.0f)));
		this->g = uint8_t(std::min(255.0f, std::max(0.0f, float(g) * float(p.g) / 255.0f)));
		this->b = uint8_t(std::min(255.0f, std::max(0.0f, float(b) * float(p.b) / 255.0f)));
		this->a = uint8_t(std::min(255.0f, std::max(0.0f, float(a) * float(p.a) / 255.0f)));
		return *this;
	}

	Pixel Pixel::inv() const
	{
		uint8_t nR = uint8_t(std::min(255, std::max(0, 255 - int(r))));
		uint8_t nG = uint8_t(std::min(255, std::max(0, 255 - int(g))));
		uint8_t nB = uint8_t(std::min(255, std::max(0, 255 - int(b))));
		return Pixel(nR, nG, nB, a);
	}

	Pixel PixelF(float red, float green, float blue, float alpha)
	{ return Pixel(uint8_t(red * 255.0f), uint8_t(green * 255.0f), uint8_t(blue * 255.0f), uint8_t(alpha * 255.0f)); }

	Pixel PixelLerp(const olc::Pixel& p1, const olc::Pixel& p2, float t)
	{ return (p2 * t) + p1 * (1.0f - t); }
*
*
*
* */
