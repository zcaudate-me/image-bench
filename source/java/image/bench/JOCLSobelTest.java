package image.bench;

import static org.jocl.CL.*;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jocl.*;

// Quick + Dirty test for https://github.com/gpu/JOCL/issues/21
// Based on the "Simple Image" example, using the kernel from 
// https://github.com/zcaudate-me/image-bench/blob/master/source/opencl/sobel.cl
public class JOCLSobelTest
{
    // Adjust these for your test
    private static final String KERNEL_FILE_NAME = "source/opencl/sobel.cl";
    private static final String IMAGE_FILE_NAME = "resources/lena-0500x0500.jpg";
    private static final long[] LOCAL_WORK_SIZE = {10, 10};

    public static void main(String args[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new JOCLSobelTest();
            }
        });
    }

    /**
     * The input image
     */
    private BufferedImage inputImage;

    /**
     * The output image
     */
    private BufferedImage outputImage;

    /**
     * The OpenCL context
     */
    private cl_context context;

    /**
     * The OpenCL command queue
     */
    private cl_command_queue commandQueue;

    /**
     * The OpenCL kernel
     */
    private cl_kernel kernel;

    /**
     * The memory object for the input image
     */
    private cl_mem inputImageMem;

    /**
     * The memory object for the output image
     */
    private cl_mem outputImageMem;

    /**
     * The width of the image
     */
    private int imageSizeX;

    /**
     * The height of the image
     */
    private int imageSizeY;

    /**
     * Creates the JOCLSimpleImage sample
     */
    public JOCLSobelTest()
    {
        inputImage = createBufferedImage(IMAGE_FILE_NAME);
        imageSizeX = inputImage.getWidth();
        imageSizeY = inputImage.getHeight();

        outputImage = new BufferedImage(
            imageSizeX, imageSizeY, BufferedImage.TYPE_INT_RGB);

        // Create the panel showing the input and output images
        JPanel mainPanel = new JPanel(new GridLayout(1,0));
        JLabel inputLabel = new JLabel(new ImageIcon(inputImage));
        mainPanel.add(inputLabel, BorderLayout.CENTER);
        JLabel outputLabel = new JLabel(new ImageIcon(outputImage));
        mainPanel.add(outputLabel, BorderLayout.CENTER);

        // Create the main frame
        JFrame frame = new JFrame("JOCL Sobel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        initCL();
        initImageMem();
        updateImage();
    }



    /**
     * Initialize the OpenCL context, command queue and kernel
     */
    public void initCL()
    {
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{device}, 
            null, null, null);
        
        // Check if images are supported
        int imageSupport[] = new int[1];
        clGetDeviceInfo (device, CL.CL_DEVICE_IMAGE_SUPPORT,
            Sizeof.cl_int, Pointer.to(imageSupport), null);
        System.out.println("Images supported: "+(imageSupport[0]==1));
        if (imageSupport[0]==0)
        {
            System.out.println("Images are not supported");
            System.exit(1);
            return;
        }

        // Create a command-queue
        System.out.println("Creating command queue...");
        long properties = 0;
        properties |= CL_QUEUE_PROFILING_ENABLE;
        //properties |= CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE;
        commandQueue = clCreateCommandQueue(context, device, properties, null);

        String programSource = "";
        try
        {
            Path path = Paths.get(KERNEL_FILE_NAME);
            programSource = Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // Create the program
        System.out.println("Creating program...");
        cl_program program = clCreateProgramWithSource(context,
            1, new String[]{ programSource }, null, null);

        // Build the program
        System.out.println("Building program...");
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        System.out.println("Creating kernel...");
        kernel = clCreateKernel(program, "sobel_uchar", null);

    }

    /**
     * Initialize the memory objects for the input and output images
     */
    private void initImageMem()
    {
        // Create the memory object for the input- and output image
        DataBufferInt dataBufferSrc =
            (DataBufferInt)inputImage.getRaster().getDataBuffer();
        int dataSrc[] = dataBufferSrc.getData();
        byte[] bytePixels = new byte[dataSrc.length];
        for (int i=0; i<dataSrc.length; i++)
        {
            bytePixels[i] = (byte)(dataSrc[i] & 0xFF);
        }
        inputImageMem = clCreateBuffer(context, 
            CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, 
            imageSizeX * imageSizeY, Pointer.to(bytePixels), null);

        outputImageMem = clCreateBuffer(context, CL_MEM_READ_WRITE, 
            imageSizeX * imageSizeY, null, null);
    }


    void updateImage()
    {
        // Set up the work size and arguments, and execute the kernel
        long globalWorkSize[] = new long[2];
        globalWorkSize[0] = imageSizeX;
        globalWorkSize[1] = imageSizeY;
        int a = 0;
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(inputImageMem));
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { 0 })); // not used
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { 0 })); // not used
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { imageSizeY })); 
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { imageSizeX }));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(outputImageMem));
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { 0 })); // not used
        clSetKernelArg(kernel, a++, Sizeof.cl_int, Pointer.to(new int[] { 0 })); // not used

        
        clEnqueueNDRangeKernel(commandQueue, kernel, 2, LOCAL_WORK_SIZE,
            globalWorkSize, null, 0, null, null);

        // Read the pixel data into the output image
        DataBufferInt dataBufferDst =
            (DataBufferInt)outputImage.getRaster().getDataBuffer();
        int dataDst[] = dataBufferDst.getData();
        byte[] bytePixels = new byte[dataDst.length];
        clEnqueueReadBuffer(commandQueue, outputImageMem, CL_TRUE, 0, 
            imageSizeX * imageSizeY, Pointer.to(bytePixels), 0,  null,  null);
        for (int i=0; i<dataDst.length; i++)
        {
            dataDst[i] = bytePixels[i];
        }
    }
    
    
    /**
     * Creates a BufferedImage of with type TYPE_INT_RGB from the
     * file with the given name.
     *
     * @param fileName The file name
     * @return The image, or null if the file may not be read
     */
    private static BufferedImage createBufferedImage(String fileName)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(fileName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        int sizeX = image.getWidth();
        int sizeY = image.getHeight();

        BufferedImage result = new BufferedImage(
            sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }
    
}